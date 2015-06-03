package jp.co.ntt.oss.heapstats.plugin.builtin.threadrecorder;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import jp.co.ntt.oss.heapstats.container.threadrecord.ThreadStat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Table cell for thread timeline.
 */
public class TimelineCell extends TableCell<ThreadStatViewModel, List<ThreadStat>> {

    private static final double LENGTH_PER_MILLS = 0.4;

    private static final double RECT_HEIGHT = 16;

    private static final String CSS_CLASS_PREFIX = "rect-";

    private HBox container;

    public TimelineCell() {
        container = new HBox(0);
        container.setAlignment(Pos.CENTER_LEFT);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(List<ThreadStat> item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null || item.size() == 0) {
            setText(null);
            setGraphic(null);
        } else {
            container.getChildren().clear();

            ThreadStatViewModel viewModel = getTableView().getItems().get(getIndex());
            LocalDateTime startTime = viewModel.getStartTime();
            LocalDateTime endTime = viewModel.getEndTime();
            LocalDateTime prevTime = startTime;
            ThreadStat.ThreadEvent prevEvent = item.get(0).getEvent();
            List<Rectangle> rects = new ArrayList<>();
            for (int i = 0; i < item.size(); i++) {
                ThreadStat threadStat = item.get(0);
                LocalDateTime currentTime = threadStat.getTime();
                rects.add(createThreadRect(prevTime, currentTime, prevEvent));

                prevTime = currentTime;
                prevEvent = threadStat.getEvent();

                if (i == item.size() - 1 && threadStat.getEvent() != ThreadStat.ThreadEvent.ThreadEnd) {
                    rects.add(createThreadRect(prevTime, endTime, prevEvent));
                }
            }
            container.getChildren().addAll(rects);
            setGraphic(container);
        }
    }

    private Rectangle createThreadRect(LocalDateTime startTime, LocalDateTime endTime,
                                       ThreadStat.ThreadEvent prevEvent) {
        long timeDiff = startTime.until(endTime, ChronoUnit.MILLIS);
        double width = LENGTH_PER_MILLS * timeDiff;
        Rectangle rectangle = new Rectangle(width, RECT_HEIGHT);
        String styleClass = CSS_CLASS_PREFIX + prevEvent.name().toLowerCase();
        rectangle.getStyleClass().add(styleClass);
        return rectangle;
    }

}
