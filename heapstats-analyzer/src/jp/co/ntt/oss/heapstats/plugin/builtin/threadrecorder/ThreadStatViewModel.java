/*
 * Copyright (C) 2015 Yasumasa Suenaga
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
package jp.co.ntt.oss.heapstats.plugin.builtin.threadrecorder;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import jp.co.ntt.oss.heapstats.container.threadrecord.ThreadStat;

/**
 * Thread Status Data Model for presentation.
 * 
 * @author Yasumasa Suenaga
 */
public class ThreadStatViewModel {
    
    private final BooleanProperty show;
    
    private long id;
    
    private String name;

    private final List<ThreadStat> threadStats;

    public ThreadStatViewModel(long id, String name, List<ThreadStat> threadStats) {
        this.id = id;
        this.name = name;
        this.threadStats = threadStats;
        this.show = new SimpleBooleanProperty(true);
    }
    
    public BooleanProperty showProperty(){
        return show;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ThreadStat> getThreadStats() {
        return threadStats;
    }

}
