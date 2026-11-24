package com.foodsaver.android.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        entities = {User.class, Commerce.class, Panier.class, Reservation.class, Evaluation.class, AppNotification.class},
        version = 6,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract CommerceDao commerceDao();
    public abstract PanierDao panierDao();
    public abstract ReservationDao   reservationDao();
    public abstract EvaluationDao    evaluationDao();
    public abstract NotificationDao  notificationDao();

    // ✅ Migration 4→5 : id long → String (Firebase UID)
    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            // Users
            db.execSQL("CREATE TABLE IF NOT EXISTS users_new (" +
                    "`id` TEXT NOT NULL PRIMARY KEY," +
                    "`email` TEXT," +
                    "`name` TEXT," +
                    "`role` TEXT," +
                    "`isSynced` INTEGER NOT NULL DEFAULT 0)");
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("ALTER TABLE users_new RENAME TO users");

            // Commerces
            db.execSQL("CREATE TABLE IF NOT EXISTS commerces_new (" +
                    "`id` TEXT NOT NULL PRIMARY KEY," +
                    "`name` TEXT," +
                    "`merchantId` TEXT," +
                    "`latitude` REAL," +
                    "`longitude` REAL," +
                    "`address` TEXT," +
                    "`city` TEXT," +
                    "`categorie` TEXT," +
                    "`imageUrl` TEXT," +
                    "`rating` REAL NOT NULL DEFAULT 0," +
                    "`quartier` TEXT," +
                    "`isSynced` INTEGER NOT NULL DEFAULT 0)");
            db.execSQL("DROP TABLE IF EXISTS commerces");
            db.execSQL("ALTER TABLE commerces_new RENAME TO commerces");

            // ✅ Paniers : recréer avec id TEXT et commerceId TEXT (sans ForeignKey)
            db.execSQL("CREATE TABLE IF NOT EXISTS paniers_new (" +
                    "`id` TEXT NOT NULL PRIMARY KEY," +
                    "`commerceId` TEXT," +
                    "`titre` TEXT," +
                    "`prix` REAL NOT NULL DEFAULT 0," +
                    "`quantite` INTEGER NOT NULL DEFAULT 0," +
                    "`description` TEXT," +
                    "`imageUrl` TEXT," +
                    "`categorie` TEXT," +
                    "`isSynced` INTEGER NOT NULL DEFAULT 0)");
            db.execSQL("DROP TABLE IF EXISTS paniers");
            db.execSQL("ALTER TABLE paniers_new RENAME TO paniers");

            // ── reservations ───────────────────────────────────────────────────────────
            db.execSQL("CREATE TABLE IF NOT EXISTS reservations_new (" +
                    "`id` TEXT NOT NULL PRIMARY KEY," +
                    "`userId` TEXT," +
                    "`panierId` TEXT," +
                    "`commerceId` TEXT," +
                    "`quantiteReservee` INTEGER NOT NULL," +
                    "`dateReservation` INTEGER NOT NULL," +
                    "`statut` TEXT," +
                    "`isSynced` INTEGER NOT NULL DEFAULT 0)");
            db.execSQL("DROP TABLE IF EXISTS reservations");
            db.execSQL("ALTER TABLE reservations_new RENAME TO reservations");

            // Evaluations
            db.execSQL("CREATE TABLE IF NOT EXISTS evaluations_new (" +
                    "`id` TEXT NOT NULL PRIMARY KEY," +
                    "`userId` TEXT," +
                    "`commerceId` TEXT," +
                    "`rating` REAL NOT NULL DEFAULT 0," +
                    "`comment` TEXT," +
                    "`dateEvaluation` INTEGER NOT NULL DEFAULT 0," +
                    "`isSynced` INTEGER NOT NULL DEFAULT 0)");
            db.execSQL("DROP TABLE IF EXISTS evaluations");
            db.execSQL("ALTER TABLE evaluations_new RENAME TO evaluations");
        }
    };

    // ✅ Migration 5→6 : création de la table notifications
    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `notifications` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`userId` TEXT NOT NULL," +
                    "`title` TEXT," +
                    "`message` TEXT," +
                    "`dateTime` INTEGER NOT NULL," +
                    "`isRead` INTEGER NOT NULL)");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "foodsaver_db"
                            )
                            .allowMainThreadQueries()
                            .addMigrations(MIGRATION_4_5, MIGRATION_5_6)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}