package menion.android.whereyougo.maps.mapsforge.overlay;

import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * A {@code Marker} draws a {@link Drawable} at a given geographical position.
 */
public class LabelMarker extends Marker {
  static Paint labelPaint;
  static Paint labelBgPaint;

  static {
    labelPaint = new Paint();
    labelPaint.setStyle(android.graphics.Paint.Style.STROKE);
    // labelPaint.setTextAlign(Align.CENTER);
    labelBgPaint = new Paint();
    labelBgPaint.setColor(Color.argb(192, 255, 255, 255));
    labelBgPaint.setStyle(android.graphics.Paint.Style.FILL);
    // labelBgPaint.setTextAlign(Align.CENTER);
  }

  protected String label;
  protected String description;
  protected boolean markerVisible = true;
  protected boolean labelVisible = true;

  /**
   * @param geoPoint the initial geographical coordinates of this marker (may be null).
   * @param drawable the initial {@code Drawable} of this marker (may be null).
   */
  public LabelMarker(GeoPoint geoPoint, Drawable drawable) {
    this(geoPoint, drawable, null, null);
  }

  /**
   * @param geoPoint the initial geographical coordinates of this marker (may be null).
   * @param drawable the initial {@code Drawable} of this marker (may be null).
   * @param label the initial caption of this marker (may be null).
   */
  public LabelMarker(GeoPoint geoPoint, Drawable drawable, String label) {
    this(geoPoint, drawable, label, null);
  }

  /**
   * @param geoPoint the initial geographical coordinates of this marker (may be null).
   * @param drawable the initial {@code Drawable} of this marker (may be null).
   * @param label the initial caption of this marker (may be null).
   * @param description the initial description of this marker (may be null).
   */
  public LabelMarker(GeoPoint geoPoint, Drawable drawable, String label, String description) {
    super(geoPoint, drawable);
    this.label = label;
    this.description = description;
  }

  @Override
  public synchronized boolean draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas,
      Point canvasPosition) {
    if (markerVisible && !super.draw(boundingBox, zoomLevel, canvas, canvasPosition))
      return false;
    if (labelVisible && label == null)
      return false;
    if (!labelVisible)
      return true;

    GeoPoint geoPoint = getGeoPoint();
    Drawable drawable = getDrawable();

    double latitude = geoPoint.latitude;
    double longitude = geoPoint.longitude;
    int pixelX =
        (int) (MercatorProjection.longitudeToPixelX(longitude, zoomLevel) - canvasPosition.x);
    int pixelY =
        (int) (MercatorProjection.latitudeToPixelY(latitude, zoomLevel) - canvasPosition.y);

    Rect drawableBounds = drawable.copyBounds();
    int left = pixelX + drawableBounds.left;
    int top = pixelY + drawableBounds.top;
    int right = pixelX + drawableBounds.right;
    int bottom = pixelY + drawableBounds.bottom;

    Rect text = new Rect();
    labelPaint.getTextBounds(label, 0, label.length(), text);
    int x = (left + right) / 2 - text.width() / 2;
    int y = bottom;
    int margin = 2;
    Rect r =
        new Rect(x - margin, y - margin, x + text.width() + margin, y + text.height() + margin);
    canvas.drawRect(r, labelBgPaint);
    canvas.drawRect(r, labelPaint);
    canvas.drawText(label, x, y + text.height(), labelPaint);

    return true;
  }

  /**
   * @return the description of this marker (may be null).
   */
  public synchronized String getDescription() {
    return this.description;
  }

  /**
   * @return the label of this marker (may be null).
   */
  public synchronized String getLabel() {
    return this.label;
  }

  public boolean isLabelVisible() {
    return labelVisible;
  }

  public boolean isMarkerVisible() {
    return markerVisible;
  }

  /**
   * @param new description of this marker (may be null).
   */
  public synchronized void setDescription(String description) {
    this.description = description;
  }

  /**
   * @param new label of this marker (may be null).
   */
  public synchronized void setLabel(String label) {
    this.label = label;
  }

  public void setLabelVisible(boolean labelVisible) {
    this.labelVisible = labelVisible;
  }

  public void setMarkerVisible(boolean markerVisible) {
    this.markerVisible = markerVisible;
  }



}
