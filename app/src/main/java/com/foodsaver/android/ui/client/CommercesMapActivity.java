package com.foodsaver.android.ui.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.foodsaver.android.R;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.data.Panier;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;
import java.util.Locale;

public class CommercesMapActivity extends BaseActivity {

    private static final GeoPoint AGADIR_CENTER = new GeoPoint(30.4278, -9.5981);
    private static final double   AGADIR_ZOOM   = 13.0;
    private static final int      REQUEST_LOC   = 1001;

    private MapView              mapView;
    private CommerceRepository   commerceRepo;
    private PanierRepository     panierRepo;
    private MyLocationNewOverlay myLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_commerces_map);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> goHome());

        commerceRepo = new CommerceRepository(this);
        panierRepo   = new PanierRepository(this);

        mapView = findViewById(R.id.map);
        setupMap();
        addCommerceMarkers();
        setupSearchMap();

        FloatingActionButton fab = findViewById(R.id.fabMyLocation);
        if (fab != null) fab.setOnClickListener(v -> centerOnUser());
    }

    private void setupSearchMap() {
        SearchView searchMap = findViewById(R.id.searchMap);
        if (searchMap == null) return;
        List<Commerce> allWithLocation = commerceRepo.getCommercesWithLocation();
        searchMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query == null || query.trim().isEmpty()) return false;
                String q = query.trim().toLowerCase(Locale.getDefault());
                for (Commerce c : allWithLocation) {
                    if (c.latitude == null || c.longitude == null) continue;
                    if ((c.name    != null && c.name.toLowerCase(Locale.getDefault()).contains(q))
                     || (c.quartier != null && c.quartier.toLowerCase(Locale.getDefault()).contains(q))
                     || (c.address  != null && c.address.toLowerCase(Locale.getDefault()).contains(q))) {
                        centerOnCommerceAndShowSheet(c);
                        return true;
                    }
                }
                Toast.makeText(CommercesMapActivity.this,
                    "Aucun commerce trouvé pour \"" + query + "\"", Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override public boolean onQueryTextChange(String t) { return false; }
        });
    }

    private void centerOnCommerceAndShowSheet(Commerce c) {
        mapView.getController().animateTo(new GeoPoint(c.latitude, c.longitude));
        mapView.getController().setZoom(17.0);
        showSheet(c);
    }

    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(AGADIR_ZOOM);
        mapView.getController().setCenter(AGADIR_CENTER);
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);
    }

    private void addCommerceMarkers() {
        for (Commerce c : commerceRepo.getCommercesWithLocation()) {
            if (c.latitude == null || c.longitude == null) continue;
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(c.latitude, c.longitude));
            marker.setTitle(c.name);
            marker.setSnippet((c.categorie != null ? c.categorie : "")
                + (c.quartier != null ? " · " + c.quartier : ""));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(getMarkerIcon(c.categorie));
            marker.setOnMarkerClickListener((m, map) -> { showSheet(c); return true; });
            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    private android.graphics.drawable.Drawable getMarkerIcon(String categorie) {
        android.graphics.drawable.Drawable d = ContextCompat.getDrawable(
            this, org.osmdroid.library.R.drawable.marker_default);
        if (d == null) return null;
        d = d.mutate();
        int color;
        if (categorie == null) { color = 0xFF2196F3; }
        else switch (categorie.toLowerCase()) {
            case "restaurant":  color = 0xFFE53935; break;
            case "snack":       color = 0xFFFF6B35; break;
            case "cafe":        color = 0xFF795548; break;
            case "boulangerie": color = 0xFFFFC107; break;
            case "epicerie":    color = 0xFF4CAF50; break;
            default:            color = 0xFF2196F3; break;
        }
        d.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        return d;
    }

    private void showSheet(Commerce c) {
        if (c.latitude != null && c.longitude != null) {
            mapView.getController().animateTo(new GeoPoint(c.latitude, c.longitude));
            mapView.getController().setZoom(16.0);
        }

        // ✅ getPaniersByCommerce prend String directement
        List<Panier> paniers = panierRepo.getPaniersByCommerce(c.id);
        int count = 0; double minPrix = 0; boolean first = true;
        for (Panier p : paniers) {
            if (p.quantite > 0) {
                count += p.quantite;
                if (first || p.prix < minPrix) { minPrix = p.prix; first = false; }
            }
        }

        View sheetView;
        try {
            sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_commerce_map, null);
        } catch (Exception e) {
            Toast.makeText(this, c.name, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PaniersActivity.class)
                .putExtra("commerceId",   c.id)   // ✅ String
                .putExtra("commerceName", c.name));
            return;
        }

        BottomSheetDialog sheet = new BottomSheetDialog(this);
        sheet.setContentView(sheetView);

        setText(sheetView, R.id.bsCommerceName, c.name);
        String addr = c.quartier != null ? c.quartier + (c.city != null ? ", " + c.city : "")
                    : c.address  != null ? c.address  + (c.city != null ? ", " + c.city : "")
                    : (c.city    != null ? c.city : "Agadir");
        setText(sheetView, R.id.bsAddress,    "📍 " + addr);
        setText(sheetView, R.id.bsMealsCount, getString(R.string.meals_to_save, count));

        TextView tvPrice = sheetView.findViewById(R.id.bsPriceFrom);
        if (tvPrice != null) {
            tvPrice.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
            if (count > 0) tvPrice.setText(getString(R.string.price_from, minPrix));
        }

        ImageButton btnClose = sheetView.findViewById(R.id.bsClose);
        if (btnClose != null) btnClose.setOnClickListener(v -> sheet.dismiss());

        View btnCmd = sheetView.findViewById(R.id.bsBtnCommander);
        if (btnCmd != null) {
            btnCmd.setEnabled(count > 0);
            int finalCount = count;
            btnCmd.setOnClickListener(v -> {
                sheet.dismiss();
                if (finalCount == 0) {
                    Toast.makeText(this, "Aucun panier disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(this, PaniersActivity.class)
                    .putExtra("commerceId",   c.id)   // ✅ String
                    .putExtra("commerceName", c.name));
            });
        }
        sheet.show();
    }

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text != null ? text : "");
    }

    private void centerOnUser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOC);
            return;
        }
        if (myLocationOverlay != null && myLocationOverlay.getMyLocation() != null) {
            mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            mapView.getController().setZoom(15.0);
        } else {
            Toast.makeText(this, "Position introuvable — activez le GPS", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int req, @NonNull String[] perms, @NonNull int[] grants) {
        super.onRequestPermissionsResult(req, perms, grants);
        if (req == REQUEST_LOC && grants.length > 0
                && grants[0] == PackageManager.PERMISSION_GRANTED
                && myLocationOverlay != null) {
            myLocationOverlay.enableMyLocation();
        }
    }

    @Override protected void onResume()     { super.onResume();  mapView.onResume();  }
    @Override protected void onPause()      { super.onPause();   mapView.onPause();   }
    @Override public void onBackPressed()   { finish(); }
}
