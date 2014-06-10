/*
 * Copyright (C) 2014 Yasumasa Suenaga
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package jp.co.ntt.oss.heapstats.plugin;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

/**
 * Base class for HeapStats FX Analyzer plugin.
 * 
 * @author Yasumasa Suenaga
 */
public abstract class PluginController implements Initializable{
    
    /** License indication for GPLv2 */
    public static final String LICENSE_GPL_V2 = "GNU General Public License version 2";
    
    /** License indication for BSD License */
    public static final String LICENSE_BSD = "Berkeley Software Distribution License";

    private Region veil;
    
    private ProgressIndicator progress;
    
    public abstract String getPluginName();
    
    /**
     * Getter of license of this plugin.
     * 
     * @return License of this plugin.
     */
    public abstract String getLicense();
    
    /**
     * Getter of license map which is used by this plugin.
     * Key is library name, value is license of library.
     * 
     * @return License of libraryes.
     */
    public abstract Map<String, String> getLibraryLicense();
    
    /**
     * Event handler when tab of this plugin is selected.
     * 
     * @return Event handler of this plugin.
     */
    public abstract EventHandler<Event> getOnPluginTabSelected();

    /**
     * Setter of veil region.
     * This region is used for veiling (e.g. showing progress)
     * 
     * @param veil 
     */
    public void setVeil(Region veil){
        this.veil = veil;
    }
    
    /**
     * Setter of progress indicator.
     * This region is used for veiling (e.g. showing progress)
     * 
     * @param progress
     */
    public void setProgress(ProgressIndicator progress){
        this.progress = progress;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    /**
     * Task binder.
     * This method binds veil and progress indicator to task.
     * 
     * @param task Task to be binded.
     */
    public void bindTask(Task task){
        veil.visibleProperty().bind(task.runningProperty());
        progress.visibleProperty().bind(task.runningProperty());
        progress.progressProperty().bind(task.progressProperty());
    }
 
    /**
     * Comvenient method of chart.
     * This method addes value to chart as percent and add Tooltip.
     * 
     * @param series Series to be added.
     * @param xData X value.
     * @param yData Y value. This value must be percentage.
     * @param label Label of this plot.
     * @param needHighlight Highlight plot if this value is set to true.
     */
    protected void addChartDataAsPercent(XYChart.Series<String, Double> series, String xData, Double yData, String label, boolean needHighlight){
        XYChart.Data<String, Double> data = new XYChart.Data<>(xData, yData);
        series.getData().add(data);

        String tip = String.format("%s: %s, %.02f %% %s", series.getName(), xData, yData, label);
        Tooltip.install(data.getNode(), new Tooltip(tip));

        if(needHighlight){
            data.getNode().setStyle("-fx-background-color: black, black;");
        }
        
    }
    
    /**
     * Comvenient method of chart.
     * This method addes value to chart as long value and add Tooltip.
     * 
     * @param series Series to be added.
     * @param xData X value.
     * @param yData Y value.
     * @param unit Unit of this value.
     * @param needHighlight Highlight plot if this value is set to true.
     */
    protected void addChartDataLong(XYChart.Series<String, Long> series, String xData, Long yData, String unit, boolean needHighlight){
        XYChart.Data<String, Long> data = new XYChart.Data<>(xData, yData);
        series.getData().add(data);

        String tip = String.format("%s: %s, %d %s", series.getName(), xData, yData, unit);
        Tooltip.install(data.getNode(), new Tooltip(tip));
        
        if(needHighlight){
            data.getNode().setStyle("-fx-background-color: black, black;");
        }
        
    }
    
    /**
     * This class represents license of libraries which are used by each plugin.
     */
    public static class LibraryLicense{
        
        private final String pluginName;
        
        private final String libraryName;
        
        private final String license;

        public LibraryLicense(String pluginName, String libraryName, String license) {
            this.pluginName = pluginName;
            this.libraryName = libraryName;
            this.license = license;
        }

        public String getPluginName() {
            return pluginName;
        }

        public String getLibraryName() {
            return libraryName;
        }

        public String getLicense() {
            return license;
        }
        
    }

}
