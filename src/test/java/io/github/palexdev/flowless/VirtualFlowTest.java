package io.github.palexdev.flowless;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Rectangle;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class VirtualFlowTest extends FlowlessTestBase {

    @Test
    public void idempotentShowTest() {
        // create VirtualFlow with 1 big cell
        Rectangle rect = new Rectangle(500, 500);
        ObservableList<Rectangle> items = FXCollections.singletonObservableList(rect);
        VirtualFlow<Rectangle, Cell<Rectangle, Rectangle>> vf = VirtualFlow.createVertical(items, Cell::wrapNode);
        vf.resize(100, 100); // size of VirtualFlow less than that of the cell
        vf.layout();

        vf.show(110.0);
        vf.show(110.0);
        vf.layout();
        assertEquals(-10.0, rect.getBoundsInParent().getMinY(), 0.01);
    }

    @Test
    public void idempotentSetLengthOffsetTest() {
        // create VirtualFlow with 1 big cell
        Rectangle rect = new Rectangle(500, 500);
        ObservableList<Rectangle> items = FXCollections.singletonObservableList(rect);
        VirtualFlow<Rectangle, Cell<Rectangle, Rectangle>> vf = VirtualFlow.createVertical(items, Cell::wrapNode);
        vf.resize(100, 100); // size of VirtualFlow less than that of the cell
        vf.layout();

        setLengthOffset(vf);
        setLengthOffset(vf);

        vf.layout();
        assertEquals(-10.0, rect.getBoundsInParent().getMinY(), 0.01);
    }

    private void setLengthOffset(VirtualFlow<Rectangle, Cell<Rectangle, Rectangle>> vf) {
        try {
            Method method = VirtualFlow.class.getDeclaredMethod("setLengthOffset", double.class);
            method.setAccessible(true);
            method.invoke(vf, 10.0);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fastVisibleIndexTest() {
        ObservableList<Rectangle> items = FXCollections.observableArrayList();
        for (int i = 0; i < 100; i++) {
            items.add(new Rectangle(500, 100));
        }

        VirtualFlow<Rectangle, Cell<Rectangle, Rectangle>> vf = VirtualFlow.createVertical(items, Cell::wrapNode);
        vf.resize(100, 450); // size of VirtualFlow enough to show several cells
        vf.layout();

        ObservableList<Cell<Rectangle, Rectangle>> visibleCells = vf.visibleCells();

        vf.show(0);
        vf.layout();
        assertSame(visibleCells.get(0), vf.getCell(vf.getFirstVisibleIndex()));
        assertSame(visibleCells.get(visibleCells.size() - 1), vf.getCell(vf.getLastVisibleIndex()));
        assertTrue(vf.getFirstVisibleIndex() <= 0 && 0 <= vf.getLastVisibleIndex());

        vf.show(50);
        vf.layout();
        assertSame(visibleCells.get(0), vf.getCell(vf.getFirstVisibleIndex()));
        assertSame(visibleCells.get(visibleCells.size() - 1), vf.getCell(vf.getLastVisibleIndex()));
        assertTrue(vf.getFirstVisibleIndex() <= 50 && 50 <= vf.getLastVisibleIndex());

        vf.show(99);
        vf.layout();
        assertSame(visibleCells.get(0), vf.getCell(vf.getFirstVisibleIndex()));
        assertSame(visibleCells.get(visibleCells.size() - 1), vf.getCell(vf.getLastVisibleIndex()));
        assertTrue(vf.getFirstVisibleIndex() <= 99 && 99 <= vf.getLastVisibleIndex());
    }
}
