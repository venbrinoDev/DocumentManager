/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package company.android.documentmanager.office.datatatdhd.achartengine.chart;

import company.android.documentmanager.office.datatatdhd.achartengine.model.XYMultipleSeriesDataset;
import company.android.documentmanager.office.datatatdhd.achartengine.model.XYValueSeries;
import company.android.documentmanager.office.datatatdhd.achartengine.renderers.SimpleSeriesRenderer;
import company.android.documentmanager.office.datatatdhd.achartengine.renderers.XYMultipleSeriesRenderer;
import company.android.documentmanager.office.datatatdhd.achartengine.renderers.XYSeriesRenderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

/**
 * The bubble chart rendering class.
 */
public class BubbleChart extends XYChart {
  /** The constant to identify this chart type. */
  public static final String TYPE = "Bubble";
  /** The legend shape width. */
  private static final int SHAPE_WIDTH = 10;
  /** The minimum bubble size. */
  private static final int MIN_BUBBLE_SIZE = 2;
  /** The maximum bubble size. */
  private static final int MAX_BUBBLE_SIZE = 20;

  BubbleChart() {
  }

  /**
   * Builds a new bubble chart instance.
   * 
   *  dataset the multiple series dataset
   *  renderer the multiple series renderer
   */
  public BubbleChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
    super(dataset, renderer);
  }

  /**
   * The graphical representation of a series.
   * 
   *  canvas the canvas to paint to
   *  paint the paint to be used for drawing
   *  points the array of points to be used for drawing the series
   *  seriesRenderer the series renderer
   *  yAxisValue the minimum value of the y axis
   *  seriesIndex the index of the series currently being drawn
   */
  public void drawSeries(Canvas canvas, Paint paint, float[] points,
      SimpleSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex) {
    XYSeriesRenderer renderer = (XYSeriesRenderer) seriesRenderer;
    paint.setColor(renderer.getColor());
    paint.setStyle(Style.FILL);
    int length = points.length;
    XYValueSeries series = (XYValueSeries) mDataset.getSeriesAt(seriesIndex);
    double max = series.getMaxValue();

    double coef = MAX_BUBBLE_SIZE / max;
    for (int i = 0; i < length; i += 2) {
      double size = series.getValue(i / 2) * coef + MIN_BUBBLE_SIZE;
      drawCircle(canvas, paint, points[i], points[i + 1], (float) size);
    }
  }

  /**
   * Returns the legend shape width.
   * 
   *  seriesIndex the series index
   * @return the legend shape width
   */
  public int getLegendShapeWidth(int seriesIndex) {
    return SHAPE_WIDTH;
  }

  /**
   * The graphical representation of the legend shape.
   * 
   *  canvas the canvas to paint to
   *  renderer the series renderer
   *  x the x value of the point the shape should be drawn at
   *  y the y value of the point the shape should be drawn at
   *  seriesIndex the series index
   *  paint the paint to be used for drawing
   */
  public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y,
      int seriesIndex, Paint paint) {
    paint.setStyle(Style.FILL);
    drawCircle(canvas, paint, x + SHAPE_WIDTH, y, 3);
  }

  /**
   * The graphical representation of a circle point shape.
   * 
   *  canvas the canvas to paint to
   *  paint the paint to be used for drawing
   *  x the x value of the point the shape should be drawn at
   *  y the y value of the point the shape should be drawn at
   *  radius the bubble radius
   */
  private void drawCircle(Canvas canvas, Paint paint, float x, float y, float radius) {
    canvas.drawCircle(x, y, radius, paint);
  }

  /**
   * Returns the chart type identifier.
   * 
   * @return the chart type
   */
  public String getChartType() {
    return TYPE;
  }

}
