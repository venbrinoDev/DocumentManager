package company.android.documentmanager.FileReaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.text.BoringLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.TextureView;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import jxl.Cell;
import jxl.CellView;
import jxl.Image;
import jxl.Range;
import jxl.Sheet;
import jxl.biff.SheetRangeImpl;
import jxl.common.LengthUnit;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.Font;
import jxl.format.RGB;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;


public class XlsSheetView extends TextureView {
    private static final int DEFAULT_TEXT_COLOR = -16777216;
    private static final float DEFAULT_TEXT_PADDING = 2.0f;
    private static final float DEFAULT_TEXT_SIZE = 12.0f;
    private static final float FIXED_CELL_HEIGHT = 25.0f;
    private static final float FIXED_CELL_WIDTH = 45.0f;
    private static final float FONT_SCALE_FACTOR = 1.25f;
    private static final float H_SCALE_FACTOR = 14.75f;
    private static final float IMAGE_H_SCALE_FACTOR = 1.29f;
    private static final float IMAGE_W_SCALE_FACTOR = 1.19f;
    private static final float MAX_ZOOM = 4.0f;
    private static final float W_SCALE_FACTOR = 36.4f;
    private int cellTextPadding;
    private int[] columnSizes;
    private float dp = 0.0f;
    private int fixColumnOffset;
    private int fixRowOffset;
    private int fixedColumnSize;
    private int fixedColumns;
    private int fixedHeight;
    private int fixedRowSize;
    private int fixedRows;
    private int fixedWidth;
    private HashMap<Integer, Bitmap> imagesCache = new HashMap<>();
    private HashMap<String, Layout> layoutsMap = new HashMap<>();
    private ArrayList<Range> mergedCells;
    private GestureDetector moveDetector;

    public AtomicBoolean needRedraw = new AtomicBoolean(true);
    private HashSet<String> overflowCells;
    private Paint paint;

    public float previousZoom = 1.0f;
    private int[] rowSizes;
    private ScaleGestureDetector scaleDetector;
    private Scroller scroller;
    private Sheet sheet;
    private int sheetHeight;
    private boolean sheetReady = false;
    private int sheetWidth;
    private TextPaint textPaint;
    private Rect tmpRect = new Rect();
    private RectF tmpRectF = new RectF();
    private HashSet<String> unusedLayoutsMap = new HashSet<>();

    public float zoom = 1.0f;

    private float clamp(float f, float f2, float f3) {
        return f < f2 ? f2 : f > f3 ? f3 : f;
    }

    private boolean isNeedClip(int i, int i2, int i3, int i4, boolean z, boolean z2, int i5, int i6) {
        return (!z && i - i3 < i5) || (!z2 && i2 - i4 < i6);
    }

    private boolean isOnScreen(int i, int i2, boolean z, int i3, int i4, int i5) {
        if (z) {
            i5 = 0;
        }
        return i3 + i5 <= i2 + i && i <= i4;
    }

    private boolean isOnScreen(int i, boolean z, int i2, int i3, int i4) {
        if (z) {
            i4 = 0;
        }
        return i2 + i4 <= i && i <= i3;
    }

    public XlsSheetView(Context context) {
        super(context);
        setSurfaceTextureListener(new SurfaceTextureListener() {
            private Thread renderThread;

            public AtomicBoolean surfaceReady;

            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                this.surfaceReady = new AtomicBoolean(true);
                this.renderThread = new Thread(new Runnable() {
                    public void run() {
                        while (surfaceReady.get()) {
                            if (XlsSheetView.this.needRedraw.get()) {
                                XlsSheetView.this.needRedraw.set(false);
                                boolean z = true;
                                while (z) {
                                    Canvas lockCanvas = XlsSheetView.this.lockCanvas();
                                    if (lockCanvas != null) {
                                        boolean access$200 = XlsSheetView.this.drawSheet(lockCanvas);
                                        XlsSheetView.this.unlockCanvasAndPost(lockCanvas);
                                        z = access$200;
                                    } else {
                                        z = false;
                                    }
                                }
                            }
                        }
                    }
                });
                this.renderThread.start();
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                this.surfaceReady.set(false);
                boolean z = true;
                while (z) {
                    try {
                        this.renderThread.join();
                        z = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    public XlsSheetView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setSurfaceTextureListener(new SurfaceTextureListener() {
            private Thread renderThread;

            public AtomicBoolean surfaceReady;

            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                this.surfaceReady = new AtomicBoolean(true);
                this.renderThread = new Thread(new Runnable() {
                    public void run() {
                        while (surfaceReady.get()) {
                            if (XlsSheetView.this.needRedraw.get()) {
                                XlsSheetView.this.needRedraw.set(false);
                                boolean z = true;
                                while (z) {
                                    Canvas lockCanvas = XlsSheetView.this.lockCanvas();
                                    if (lockCanvas != null) {
                                        boolean access$200 = XlsSheetView.this.drawSheet(lockCanvas);
                                        XlsSheetView.this.unlockCanvasAndPost(lockCanvas);
                                        z = access$200;
                                    } else {
                                        z = false;
                                    }
                                }
                            }
                        }
                    }
                });
                this.renderThread.start();
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                this.surfaceReady.set(false);
                boolean z = true;
                while (z) {
                    try {
                        this.renderThread.join();
                        z = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    public XlsSheetView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setSurfaceTextureListener(new SurfaceTextureListener() {
            private Thread renderThread;

            public AtomicBoolean surfaceReady;

            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                this.surfaceReady = new AtomicBoolean(true);
                this.renderThread = new Thread(new Runnable() {
                    public void run() {
                        while (surfaceReady.get()) {
                            if (XlsSheetView.this.needRedraw.get()) {
                                XlsSheetView.this.needRedraw.set(false);
                                boolean z = true;
                                while (z) {
                                    Canvas lockCanvas = XlsSheetView.this.lockCanvas();
                                    if (lockCanvas != null) {
                                        boolean access$200 = XlsSheetView.this.drawSheet(lockCanvas);
                                        XlsSheetView.this.unlockCanvasAndPost(lockCanvas);
                                        z = access$200;
                                    } else {
                                        z = false;
                                    }
                                }
                            }
                        }
                    }
                });
                this.renderThread.start();
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                this.surfaceReady.set(false);
                boolean z = true;
                while (z) {
                    try {
                        this.renderThread.join();
                        z = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    public void setSheet(Sheet sheet2) {
        int i;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.textPaint = new TextPaint(this.paint);
        this.textPaint.setTextSize(this.dp * 18.0f);
        this.dp = getContext().getResources().getDisplayMetrics().density;
        float f = this.dp;
        this.fixedWidth = (int) (FIXED_CELL_WIDTH * f);
        this.fixedHeight = (int) (FIXED_CELL_HEIGHT * f);
        this.cellTextPadding = (int) (DEFAULT_TEXT_PADDING * f);
        this.sheet = sheet2;
        float f2 = f / H_SCALE_FACTOR;
        float f3 = f / W_SCALE_FACTOR;
        int i2 = this.fixedWidth;
        int i3 = this.fixedHeight;
        this.columnSizes = new int[(sheet2.getColumns() + 1)];
        int i4 = i2;
        for (int i5 = 0; i5 < sheet2.getColumns(); i5++) {
            this.columnSizes[i5] = i4;
            CellView columnView = sheet2.getColumnView(i5);
            if (!columnView.isHidden()) {
                i4 += (int) (((float) columnView.getSize()) * f3);
            }
        }
        this.columnSizes[sheet2.getColumns()] = i4;
        this.rowSizes = new int[(sheet2.getRows() + 1)];
        for (int i6 = 0; i6 < sheet2.getRows(); i6++) {
            this.rowSizes[i6] = i3;
            CellView rowView = sheet2.getRowView(i6);
            if (!rowView.isHidden()) {
                i3 += (int) (((float) rowView.getSize()) * f2);
            }
        }
        this.rowSizes[sheet2.getRows()] = i3;
        this.sheetWidth = i4;
        this.sheetHeight = i3;
        this.fixedColumns = sheet2.getSettings().getHorizontalFreeze();
        this.fixedRows = sheet2.getSettings().getVerticalFreeze();
        int[] iArr = this.columnSizes;
        int i7 = this.fixedColumns;
        this.fixedColumnSize = iArr[i7];
        this.fixedRowSize = this.rowSizes[this.fixedRows];
        if (i7 == 0) {
            i = 0;
        } else {
            i = (int) (this.dp * 3.0f);
        }
        this.fixColumnOffset = i;
        this.fixRowOffset = this.fixedRows == 0 ? 0 : (int) (this.dp * 3.0f);
        this.mergedCells = new ArrayList<>();
        Collections.addAll(this.mergedCells, sheet2.getMergedCells());
        this.overflowCells = new HashSet<>();
        for (int i8 = 0; i8 < sheet2.getColumns(); i8++) {
            for (int i9 = 0; i9 < sheet2.getRows(); i9++) {
                Cell cell = sheet2.getCell(i8, i9);
                String contents = cell.getContents();
                if (!isEmpty(cell.getContents()) && !contents.contains("\n")) {
                    initTextPaint(cell.getCellFormat());
                    float measureText = this.textPaint.measureText(contents);
                    int[] iArr2 = this.columnSizes;
                    if (measureText > ((float) (iArr2[i8 + 1] - iArr2[i8]))) {
                        int i10 = i8;
                        while (true) {
                            int i11 = i10 + 1;
                            if (i11 >= sheet2.getColumns()) {
                                break;
                            }
                            int[] iArr3 = this.columnSizes;
                            if (measureText <= ((float) (iArr3[i11] - iArr3[i8])) || !isEmpty(sheet2.getCell(i11, i9).getContents())) {
                                break;
                            }
                            i10 = i11;
                        }
                        if (i10 != i8) {
                            ArrayList<Range> arrayList = this.mergedCells;
                            SheetRangeImpl sheetRangeImpl = new SheetRangeImpl(sheet2, i8, i9, i10, i9);
                            arrayList.add(sheetRangeImpl);
                            this.overflowCells.add(cellToId(i8, i9));
                        }
                    }
                }
            }
        }
        System.gc();
        this.scaleDetector = new ScaleGestureDetector(getContext(), new SimpleOnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                XlsSheetView.this.zoomBy(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                return super.onScale(scaleGestureDetector);
            }

            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
                XlsSheetView xlsSheetView = XlsSheetView.this;
                xlsSheetView.previousZoom = xlsSheetView.zoom;
                super.onScaleEnd(scaleGestureDetector);
            }
        });
        this.moveDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                XlsSheetView xlsSheetView = XlsSheetView.this;
                xlsSheetView.scrollBy((int) (f / xlsSheetView.zoom), (int) (f2 / XlsSheetView.this.zoom));
                return super.onScroll(motionEvent, motionEvent2, f, f2);
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                XlsSheetView.this.flingBy((int) f, (int) f2);
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }
        });
        this.scroller = new Scroller(getContext());
        this.sheetReady = true;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    private int getMaxScrollX() {
        return (int) Math.max(0.0f, ((float) this.sheetWidth) - (((float) getMeasuredWidth()) / this.zoom));
    }

    private int getMaxScrollY() {
        return (int) Math.max(0.0f, ((float) this.sheetHeight) - (((float) getMeasuredHeight()) / this.zoom));
    }

    public void scrollBy(int i, int i2) {
        Scroller scroller2 = this.scroller;
        scroller2.startScroll(0, 0, (int) clamp((float) (scroller2.getCurrX() + i), 0.0f, (float) getMaxScrollX()), (int) clamp((float) (this.scroller.getCurrY() + i2), 0.0f, (float) getMaxScrollY()), 0);
        this.scroller.computeScrollOffset();
        redraw();
    }

    public void flingBy(int i, int i2) {
        Scroller scroller2 = this.scroller;
        scroller2.fling(scroller2.getCurrX(), this.scroller.getCurrY(), -i, -i2, 0, getMaxScrollX(), 0, getMaxScrollY());
        redraw();
    }

    public void zoomBy(float f, float f2, float f3) {
        float f4 = this.zoom;
        this.zoom = clamp(this.previousZoom * f, 1.0f, MAX_ZOOM);
        float f5 = f2 / f4;
        float f6 = this.zoom;
        scrollBy((int) (f5 * ((f6 / f4) - 1.0f)), (int) ((f3 / f4) * ((f6 / f4) - 1.0f)));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.scroller.forceFinished(true);
        }
        this.moveDetector.onTouchEvent(motionEvent);
        this.scaleDetector.onTouchEvent(motionEvent);
        return true;
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i == 0) {
            redraw();
        }
    }

    public void redraw() {
        this.needRedraw.set(true);
    }

    public void invalidate() {
        super.invalidate();
    }

    private String intToColumnName(int i) {
        String str;
        String str2 = "";
        while (true) {
            str = "0ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            if (i <= 26) {
                break;
            }
            int i2 = i % 26;
            if (i2 == 0) {
                i2 = 26;
            }
            i = (i - i2) / 26;
            StringBuilder sb = new StringBuilder();
            sb.append(str.charAt(i2));
            sb.append(str2);
            str2 = sb.toString();
        }
        if (i == 0) {
            return str2;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str.charAt(i));
        sb2.append(str2);
        return sb2.toString();
    }

    private String cellToId(int i, int i2) {
        return String.format(Locale.getDefault(), "%d:%d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
    }


    /* JADX WARNING: Removed duplicated region for block: B:110:0x0291  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x02b0  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x02b6  */
    /* JADX WARNING: Removed duplicated region for block: B:267:0x02c6 A[SYNTHETIC] */
    public boolean drawSheet(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        Canvas canvas2;
        Bitmap bitmap;
        int i7;
        int i8;
        int i9;
        int i10;
        boolean z;
        int i11;
        int i12;
        Canvas canvas3;
        int i13;
        int i14;
        int i15;
        boolean z2;
        Canvas canvas4;
        int i16;
        Cell cell;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        Canvas canvas5 = canvas;
        if (!this.sheetReady) {
            return false;
        }
        this.scroller.computeScrollOffset();
        int currX = this.scroller.getCurrX();
        int currY = this.scroller.getCurrY();
        int measuredWidth = currX + getMeasuredWidth();
        int measuredHeight = currY + getMeasuredHeight();
        canvas5.drawColor(-1);
        float f = this.zoom;
        canvas5.scale(f, f);
        canvas5.translate((float) (-currX), (float) (-currY));
        this.unusedLayoutsMap.addAll(this.layoutsMap.keySet());
        this.paint.setColor(-3355444);
        int i23 = 0;
        while (true) {
            int[] iArr = this.columnSizes;
            if (i23 >= iArr.length) {
                break;
            }
            int i24 = iArr[i23];
            if (i23 <= this.fixedColumns) {
                i22 = i24 + currX;
            } else {
                i22 = i24 + this.fixColumnOffset;
            }
            int i25 = i22;
            int i26 = i25;
            if (isOnScreen(i25, i23 <= this.fixedColumns, currX, measuredWidth, this.fixedColumnSize)) {
                float f2 = (float) i26;
                canvas.drawRect(f2 - this.dp, 0.0f, f2, (float) this.sheetHeight, this.paint);
            }
            i23++;
        }
        int i27 = 0;
        while (true) {
            int[] iArr2 = this.rowSizes;
            if (i27 >= iArr2.length) {
                break;
            }
            int i28 = iArr2[i27];
            if (i27 <= this.fixedRows) {
                i21 = i28 + currY;
            } else {
                i21 = i28 + this.fixRowOffset;
            }
            int i29 = i21;
            if (isOnScreen(i29, i27 <= this.fixedRows, currY, measuredHeight, this.fixedRowSize)) {
                float f3 = (float) i29;
                canvas.drawRect(0.0f, f3 - this.dp, (float) this.sheetWidth, f3, this.paint);
            }
            i27++;
        }
        int i30 = 0;
        while (i30 < this.sheet.getColumns()) {
            int i31 = 0;
            while (i31 < this.sheet.getRows()) {
                Cell cell2 = this.sheet.getCell(i30, i31);
                CellFormat cellFormat = cell2.getCellFormat();
                int[] iArr3 = this.columnSizes;
                int i32 = iArr3[i30];
                int[] iArr4 = this.rowSizes;
                int i33 = iArr4[i31];
                int i34 = iArr3[i30 + 1] - i32;
                int i35 = i31 + 1;
                int i36 = iArr4[i35] - i33;
                if (i34 == 0 || i36 == 0) {
                    i12 = measuredHeight;
                    i11 = measuredWidth;
                    canvas3 = canvas5;
                    i13 = i30;
                } else {
                    if (i30 < this.fixedColumns) {
                        i14 = i32 + currX;
                    } else {
                        i14 = i32 + this.fixColumnOffset;
                    }
                    int i37 = i14;
                    if (i31 < this.fixedRows) {
                        i15 = i33 + currY;
                    } else {
                        i15 = i33 + this.fixRowOffset;
                    }
                    int i38 = i15;
                    int i39 = i37;
                    Cell cell3 = cell2;
                    int i40 = measuredWidth;
                    i11 = measuredWidth;
                    int i41 = i31;
                    if (isOnScreen(i37, i34, i30 < this.fixedColumns, currX, i40, this.fixedColumnSize)) {
                        if (isOnScreen(i38, i36, i41 < this.fixedRows, currY, measuredHeight, this.fixedRowSize)) {
                            Iterator<Range> it = this.mergedCells.iterator();
                            boolean z3 = true;
                            boolean z4 = false;
                            while (it.hasNext()) {
                                Range next = it.next();
                                Cell topLeft = next.getTopLeft();
                                Cell bottomRight = next.getBottomRight();
                                if (cell3 == topLeft) {
                                    i34 = this.columnSizes[bottomRight.getColumn() + 1] - i39;
                                    i20 = i38;
                                    i36 = this.rowSizes[bottomRight.getRow() + 1] - i20;
                                    z4 = true;
                                } else {
                                    i20 = i38;
                                    if (i30 >= topLeft.getColumn() && i30 <= bottomRight.getColumn() && i41 >= topLeft.getRow() && i41 <= bottomRight.getRow()) {
                                        z3 = false;
                                    }
                                }
                                i38 = i20;
                            }
                            int i42 = i38;
                            if (z3) {
                                int i43 = measuredHeight;
                                Cell cell4 = cell3;
                                i13 = i30;
                                int i44 = i42;
                                if (isNeedClip(i39, i42, currX, currY, i30 < this.fixedColumns, i41 < this.fixedRows, this.fixedColumnSize, this.fixedRowSize)) {
                                    int save = canvas.save();
                                    this.tmpRect.set(i13 < this.fixedColumns ? 0 : this.fixedColumnSize, i41 < this.fixedRows ? 0 : this.fixedRowSize, getMeasuredWidth(), getMeasuredHeight());
                                    this.tmpRect.offset(currX, currY);
                                    canvas4 = canvas;
                                    cell = cell4;
                                    canvas4.clipRect(this.tmpRect);
                                    i16 = save;
                                    z2 = true;
                                } else {
                                    canvas4 = canvas;
                                    cell = cell4;
                                    i16 = 0;
                                    z2 = false;
                                }
                                if (z4) {
                                    this.paint.setColor(-1);
                                    int i45 = i44;
                                    float f4 = this.dp;
                                    i17 = i45;
                                    canvas.drawRect((float) i39, (float) i45, ((float) (i39 + i34)) - f4, ((float) (i45 + i36)) - f4, this.paint);
                                } else {
                                    i17 = i44;
                                }
                                if (cellFormat != null) {
                                    RGB defaultRGB = cellFormat.getBackgroundColour().getDefaultRGB();
                                    int rgb = Color.rgb(defaultRGB.getRed(), defaultRGB.getGreen(), defaultRGB.getBlue());
                                    if (rgb != -1) {
                                        this.paint.setColor(rgb);
                                        float f5 = this.dp;
                                        int i46 = i17;
                                        i18 = i46;
                                        canvas.drawRect(((float) i39) - f5, ((float) i46) - f5, (float) (i39 + i34), (float) (i46 + i36), this.paint);
                                        if (cell.getContents() == null) {
                                            i19 = i16;
                                            canvas3 = canvas4;
                                            i12 = i43;
                                            drawText(cellToId(i13, i41), i39, i18, i34, i36, cell.getContents(), z4, cellFormat, canvas);
                                        } else {
                                            i19 = i16;
                                            canvas3 = canvas4;
                                            i12 = i43;
                                        }
                                        if (!z2) {
                                            canvas3.restoreToCount(i19);
                                        }
                                    }
                                }
                                i18 = i17;
                                if (cell.getContents() == null) {
                                }
                                if (!z2) {
                                }
                            }
                        }
                    }
                    canvas3 = canvas;
                    i13 = i30;
                    i12 = measuredHeight;
                }
                i30 = i13;
                canvas5 = canvas3;
                measuredHeight = i12;
                i31 = i35;
                measuredWidth = i11;
            }
            int i47 = measuredHeight;
            i30++;
            canvas5 = canvas5;
            measuredWidth = measuredWidth;
        }
        int i48 = measuredHeight;
        int i49 = measuredWidth;
        Canvas canvas6 = canvas5;
        int i50 = 0;
        while (i50 < this.sheet.getColumns()) {
            int i51 = 0;
            while (i51 < this.sheet.getRows()) {
                int[] iArr5 = this.columnSizes;
                int i52 = iArr5[i50];
                int[] iArr6 = this.rowSizes;
                int i53 = iArr6[i51];
                int i54 = iArr5[i50 + 1] - i52;
                int i55 = i51 + 1;
                int i56 = iArr6[i55] - i53;
                if (!(i54 == 0 || i56 == 0)) {
                    if (i50 < this.fixedColumns) {
                        i8 = i52 + currX;
                    } else {
                        i8 = i52 + this.fixColumnOffset;
                    }
                    int i57 = i8;
                    if (i51 < this.fixedRows) {
                        i9 = i53 + currY;
                    } else {
                        i9 = i53 + this.fixRowOffset;
                    }
                    int i58 = i9;
                    if (isOnScreen(i57, i54, i50 < this.fixedColumns, currX, i49, this.fixedColumnSize)) {
                        if (isOnScreen(i58, i56, i51 < this.fixedRows, currY, i48, this.fixedRowSize)) {
                            CellFormat cellFormat2 = this.sheet.getCell(i50, i51).getCellFormat();
                            if (cellFormat2 != null) {
                                int i59 = i54;
                                CellFormat cellFormat3 = cellFormat2;
                                int i60 = i57;
                                int i61 = i56;
                                if (isNeedClip(i57, i58, currX, currY, i50 < this.fixedColumns, i51 < this.fixedRows, this.fixedColumnSize, this.fixedRowSize)) {
                                    int save2 = canvas.save();
                                    this.tmpRect.set(i50 < this.fixedColumns ? 0 : this.fixedColumnSize, i51 < this.fixedRows ? 0 : this.fixedRowSize, getMeasuredWidth(), getMeasuredHeight());
                                    this.tmpRect.offset(currX, currY);
                                    canvas6.clipRect(this.tmpRect);
                                    i10 = save2;
                                    z = true;
                                } else {
                                    z = false;
                                    i10 = 0;
                                }
                                float f6 = (float) i60;
                                float f7 = this.dp;
                                float f8 = (float) i58;
                                float f9 = (float) i61;
                                float f10 = f9 + f7;
                                CellFormat cellFormat4 = cellFormat3;
                                BorderLineStyle borderLine = cellFormat4.getBorderLine(Border.LEFT);
                                Colour borderColour = cellFormat4.getBorderColour(Border.LEFT);
                                i7 = currY;
                                CellFormat cellFormat5 = cellFormat4;
                                BorderLineStyle borderLineStyle = borderLine;
                                float f11 = f9;
                                Colour colour = borderColour;
                                float f12 = f8;
                                drawBorder(f6 - f7, f8 - f7, f7, f10, borderLineStyle, colour, canvas);
                                float f13 = this.dp;
                                float f14 = (float) i59;
                                float f15 = f14;
                                Canvas canvas7 = canvas;
                                drawBorder(f6 - f13, f12 - f13, f14 + f13, f13, cellFormat5.getBorderLine(Border.TOP), cellFormat5.getBorderColour(Border.TOP), canvas7);
                                float f16 = this.dp;
                                drawBorder((f6 - f16) + f15, f12 - f16, f16, f11 + f16, cellFormat5.getBorderLine(Border.RIGHT), cellFormat5.getBorderColour(Border.RIGHT), canvas7);
                                float f17 = this.dp;
                                drawBorder(f6 - f17, (f12 - f17) + f11, f15 + f17, f17, cellFormat5.getBorderLine(Border.BOTTOM), cellFormat5.getBorderColour(Border.BOTTOM), canvas);
                                if (z) {
                                    canvas6.restoreToCount(i10);
                                }
                                i51 = i55;
                                currY = i7;
                            }
                        }
                    }
                }
                i7 = currY;
                i51 = i55;
                currY = i7;
            }
            int i62 = currY;
            i50++;
        }
        int i63 = currY;
        int i64 = 0;
        while (i64 < this.sheet.getNumberOfImages()) {
            Image drawing = this.sheet.getDrawing(i64);
            double column = drawing.getColumn();
            double row = drawing.getRow();
            int i65 = (int) column;
            int i66 = (int) row;
            int[] iArr7 = this.columnSizes;
            double d = (double) iArr7[i65];
            double d2 = (double) (iArr7[i65 + 1] - iArr7[i65]);
            Double.isNaN(d2);
            Double.isNaN(d);
            int i67 = (int) (d + ((column % 1.0d) * d2));
            int[] iArr8 = this.rowSizes;
            double d3 = (double) iArr8[i66];
            double d4 = (double) (iArr8[i66 + 1] - iArr8[i66]);
            Double.isNaN(d4);
            Double.isNaN(d3);
            int i68 = (int) (d3 + ((row % 1.0d) * d4));
            double d5 = (double) this.dp;
            Double.isNaN(d5);
            int width = (int) (drawing.getWidth(LengthUnit.POINTS) * 1.190000057220459d * d5);
            double d6 = (double) this.dp;
            Double.isNaN(d6);
            int height = (int) (drawing.getHeight(LengthUnit.POINTS) * 1.2899999618530273d * d6);
            if (i65 < this.fixedColumns) {
                i4 = i67 + currX;
            } else {
                i4 = i67 + this.fixColumnOffset;
            }
            int i69 = i4;
            if (i66 < this.fixedRows) {
                i5 = i68 + i63;
            } else {
                i5 = i68 + this.fixRowOffset;
            }
            int i70 = i5;
            if (isOnScreen(i69, width, i65 < this.fixedColumns, currX, i49, this.fixedColumnSize)) {
                if (isOnScreen(i70, height, i66 < this.fixedRows, i63, i48, this.fixedRowSize)) {
                    if (this.imagesCache.containsKey(Integer.valueOf(i64))) {
                        bitmap = this.imagesCache.get(Integer.valueOf(i64));
                    } else {
                        byte[] imageData = drawing.getImageData();
                        bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        this.imagesCache.put(Integer.valueOf(i64), bitmap);
                    }
                    this.tmpRect.set(i65 < this.fixedColumns ? 0 : this.fixedColumnSize, i66 < this.fixedRows ? 0 : this.fixedRowSize, getMeasuredWidth(), getMeasuredHeight());
                    i6 = i63;
                    this.tmpRect.offset(currX, i6);
                    int save3 = canvas.save();
                    canvas2 = canvas;
                    canvas2.clipRect(this.tmpRect);
                    this.tmpRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    int i71 = i70;
                    this.tmpRectF.set((float) i69, (float) i71, (float) (i69 + width), (float) (i71 + height));
                    canvas2.drawBitmap(bitmap, this.tmpRect, this.tmpRectF, this.paint);
                    canvas2.restoreToCount(save3);
                    i64++;
                    canvas6 = canvas2;
                    i63 = i6;
                }
            }
            canvas2 = canvas;
            i6 = i63;
            if (this.imagesCache.containsKey(Integer.valueOf(i64))) {
                this.imagesCache.get(Integer.valueOf(i64)).recycle();
                this.imagesCache.remove(Integer.valueOf(i64));
            }
            i64++;
            canvas6 = canvas2;
            i63 = i6;
        }
        Canvas canvas8 = canvas6;
        int i72 = i63;
        int columns = this.sheet.getColumns() - 1;
        while (columns >= 0) {
            int[] iArr9 = this.columnSizes;
            int i73 = iArr9[columns];
            int i74 = columns + 1;
            int i75 = iArr9[i74] - i73;
            if (columns < this.fixedColumns) {
                i3 = i73 + currX;
            } else {
                i3 = i73 + this.fixColumnOffset;
            }
            int i76 = i3;
            int i77 = i76;
            if (isOnScreen(i76, i75, columns < this.fixedColumns, currX, i49, this.fixedColumnSize)) {
                this.paint.setColor(-3355444);
                float f18 = (float) i77;
                float f19 = (float) i72;
                float f20 = (float) (i77 + i75);
                canvas.drawRect(f18 - this.dp, f19, f20, (float) (this.fixedHeight + i72), this.paint);
                this.paint.setColor(-789517);
                float f21 = this.dp;
                canvas.drawRect(f18, f19 + f21, f20 - f21, ((float) (this.fixedHeight + i72)) - f21, this.paint);
                this.paint.setColor(-16777216);
                drawText(cellToId(columns, -1), i77, i72, i75, this.fixedHeight, intToColumnName(i74), false, null, canvas);
            }
            columns--;
            Canvas canvas9 = canvas;
        }
        int rows = this.sheet.getRows() - 1;
        while (rows >= 0) {
            int[] iArr10 = this.rowSizes;
            int i78 = iArr10[rows];
            int i79 = iArr10[rows + 1] - i78;
            if (rows < this.fixedRows) {
                i = i78 + i72;
            } else {
                i = i78 + this.fixRowOffset;
            }
            int i80 = i;
            int i81 = i80;
            if (!isOnScreen(i80, i79, rows < this.fixedRows, i72, i48, this.fixedRowSize)) {
                i2 = rows;
            } else {
                this.paint.setColor(-3355444);
                float f22 = (float) currX;
                float f23 = (float) i81;
                float f24 = (float) (i81 + i79);
                canvas.drawRect(f22, f23 - this.dp, (float) (this.fixedWidth + currX), f24, this.paint);
                this.paint.setColor(-789517);
                float f25 = this.dp;
                canvas.drawRect(f22 + f25, f23, ((float) (this.fixedWidth + currX)) - f25, f24 - f25, this.paint);
                this.paint.setColor(-16777216);
                i2 = rows;
                drawText(cellToId(-1, rows) + rows, currX, i81, this.fixedWidth, i79, "" + intToColumnName(i79), false, null, canvas);
            }
            rows = i2 - 1;
        }
        this.paint.setColor(-3355444);
        float f26 = (float) currX;
        float f27 = (float) i72;
        canvas.drawRect(f26, f27, (float) (this.fixedWidth + currX), (float) (i72 + this.fixedHeight), this.paint);
        this.paint.setColor(-789517);
        float f28 = this.dp;
        canvas.drawRect(f26 + f28, f27 + f28, ((float) (this.fixedWidth + currX)) - f28, ((float) (i72 + this.fixedHeight)) - f28, this.paint);
        this.paint.setColor(-3355444);
        if (this.fixedColumns > 0) {
            int i82 = this.fixedColumnSize;
            canvas.drawRect(((float) (i82 + currX)) - this.dp, f27, (float) (i82 + this.fixColumnOffset + currX), (float) i48, this.paint);
        }
        if (this.fixedRows > 0) {
            int i83 = this.fixedRowSize;
            canvas.drawRect(f26, ((float) (i83 + i72)) - this.dp, (float) i49, (float) (i83 + this.fixRowOffset + i72), this.paint);
        }
        Iterator<String> it2 = this.unusedLayoutsMap.iterator();
        while (it2.hasNext()) {
            this.layoutsMap.remove(it2.next());
        }
        this.unusedLayoutsMap.clear();
        System.gc();
        return !this.scroller.isFinished();
    }


    private void drawBorder(float f, float f2, float f3, float f4, BorderLineStyle borderLineStyle, Colour colour, Canvas canvas) {
        float f5;
        if (borderLineStyle != BorderLineStyle.NONE) {
            if (borderLineStyle == BorderLineStyle.THIN) {
                f5 = 0.0f;
            } else {
                f5 = this.dp / DEFAULT_TEXT_PADDING;
            }
            if (colour.getValue() == 64) {
                this.paint.setColor(-16777216);
            } else {
                RGB defaultRGB = colour.getDefaultRGB();
                this.paint.setColor(Color.rgb(defaultRGB.getRed(), defaultRGB.getGreen(), defaultRGB.getBlue()));
            }
            canvas.drawRect(f - f5, f2 - f5, f + f3 + f5, f2 + f4 + f5, this.paint);
        }
    }

    private void initTextPaint(CellFormat cellFormat) {
        boolean z = false;
        if (cellFormat != null) {
            Font font = cellFormat.getFont();
            RGB defaultRGB = font.getColour().getDefaultRGB();
            this.textPaint.setColor(Color.rgb(defaultRGB.getRed(), defaultRGB.getGreen(), defaultRGB.getBlue()));
            this.textPaint.setTextSize(((float) font.getPointSize()) * FONT_SCALE_FACTOR * this.dp);
            this.textPaint.setTypeface(font.getBoldWeight() > 400 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            TextPaint textPaint2 = this.textPaint;
            if (font.getUnderlineStyle() != UnderlineStyle.NO_UNDERLINE) {
                z = true;
            }
            textPaint2.setUnderlineText(z);
            return;
        }
        this.textPaint.setColor(-16777216);
        this.textPaint.setTextSize(this.dp * DEFAULT_TEXT_SIZE);
        this.textPaint.setTypeface(Typeface.DEFAULT);
        this.textPaint.setUnderlineText(false);
    }

    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00e3  */
    private void drawText(String str, int i, int i2, int i3, int i4, String str2, boolean z, CellFormat cellFormat, Canvas canvas) {
        char c = 0;
        Layout.Alignment alignment;
        Layout.Alignment alignment2;
        char c2;
        char c3;
        StaticLayout staticLayout = null;
        String str3 = str;
        String str4 = str2;
        CellFormat cellFormat2 = cellFormat;
        Canvas canvas2 = canvas;
        int i5 = this.cellTextPadding;
        int i6 = i3 - (i5 * 2);
        int i7 = i4 - (i5 * 2);
        if (i6 > 0 && i7 > 0) {
            int save = canvas.save();
            int i8 = this.cellTextPadding;
            canvas2.translate((float) (i + i8), (float) (i2 + i8));
            boolean z2 = false;
            if (canvas2.clipRect(0, 0, i6, i7)) {
                initTextPaint(cellFormat2);
                if (cellFormat2 != null) {
                    if (cellFormat.getAlignment() == Alignment.RIGHT || (cellFormat.getAlignment() == Alignment.GENERAL && isRightAlignDefault(str4, cellFormat2))) {
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                    } else if (cellFormat.getAlignment() == Alignment.CENTRE) {
                        alignment = Layout.Alignment.ALIGN_CENTER;
                    } else {
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                    }
                    if (cellFormat.getVerticalAlignment() == VerticalAlignment.BOTTOM) {
                        c = 2;
                    } else if (cellFormat.getVerticalAlignment() != VerticalAlignment.CENTRE) {
                        c = 0;
                    }
                    if (z || !this.overflowCells.contains(str3)) {
                        alignment2 = alignment;
                    } else {
                        alignment2 = Layout.Alignment.ALIGN_NORMAL;
                        z2 = true;
                    }
                    BoringLayout boringLayout = null;
                    if (!this.layoutsMap.containsKey(str3)) {
                        c2 = c;
                        c3 = 1;
                        staticLayout = (StaticLayout) this.layoutsMap.get(str3);
                    } else {
                        if (z2) {
                            BoringLayout.Metrics isBoring = BoringLayout.isBoring(str4, this.textPaint);
                            if (isBoring != null) {
                                c2 = c;
                                c3 = 1;
                                boringLayout = BoringLayout.make(str2, this.textPaint, i6, alignment2, 1.0f, 0.0f, isBoring, false);
                                if (boringLayout == null) {
                                    StaticLayout staticLayout2 = new StaticLayout(str2, this.textPaint, i6, alignment2, 1.0f, 0.0f, false);
                                }
                                this.layoutsMap.put(str3, boringLayout);
                            }
                        }
                        c2 = c;
                        c3 = 1;
                        if (boringLayout == null) {
                        }
                        this.layoutsMap.put(str3, boringLayout);
                    }
                    this.unusedLayoutsMap.remove(str3);
                    if (staticLayout.getHeight() <= i7) {
                        if (c2 == c3) {
                            canvas2.translate(0.0f, (float) ((i7 - staticLayout.getHeight()) / 2));
                        } else if (c2 == 2) {
                            canvas2.translate(0.0f, (float) (i7 - staticLayout.getHeight()));
                        }
                    }
                    staticLayout.draw(canvas2);
                } else {
                    alignment = Layout.Alignment.ALIGN_CENTER;
                }
                c = 1;
                if (z) {
                }
                alignment2 = alignment;
                BoringLayout boringLayout2 = null;
                if (!this.layoutsMap.containsKey(str3)) {
                }
                this.unusedLayoutsMap.remove(str3);
                if (staticLayout.getHeight() <= i7) {
                }
                staticLayout.draw(canvas2);
            }
            canvas2.restoreToCount(save);
        }
    }



    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0011, code lost:
        if (company.android.documentmanager.office.fc.codec.StringUtils.isEmpty(r3.getFormat().getFormatString()) == false) goto L_0x0013;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0013, code lost:
        return true;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0005 */
    private boolean isRightAlignDefault(String str, CellFormat cellFormat) {
        Double.valueOf(str);
        return true;
    }

}
