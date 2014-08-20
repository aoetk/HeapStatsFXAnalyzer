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

package jp.co.ntt.oss.heapstats.plugin.builtin.snapshot.handler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import javafx.concurrent.Task;
import jp.co.ntt.oss.heapstats.container.ObjectData;
import jp.co.ntt.oss.heapstats.container.SnapShotHeader;
import jp.co.ntt.oss.heapstats.parser.HeapStatsParser;

/**
 * Task to parse SnapSHot.
 * 
 * @author Yasumasa Suenaga
 */
public class SnapShotParseTask extends Task<Void>{
    
    private final List<SnapShotHeader> snapShotList;
    
    private Map<SnapShotHeader, Map<Long, ObjectData>> snapShots;
    
    public SnapShotParseTask(List<SnapShotHeader> snapShotList) {
        this.snapShotList = snapShotList;
    }
    
    /**
     * Parse SnapShot with given header.
     * 
     * @param progress Progress counter.
     * @param header SnapShot header which you want to.
     */
    private void parseFile(LongAdder progress, SnapShotHeader header){
        progress.increment();
        SnapShotHandler handler = new SnapShotHandler();
        HeapStatsParser parser = new HeapStatsParser();

        try{
            parser.parseSingle(header, handler);
        }
        catch(IOException e){
            throw new UncheckedIOException(e);
        }

        snapShots.put(header, handler.getSnapShot());
        updateProgress(progress.longValue(), snapShotList.size());
    }

    @Override
    protected Void call() throws Exception {
        
        /* Parse SnapShot */
        snapShots = new ConcurrentHashMap<>();
        LongAdder progress = new LongAdder();

        snapShotList.parallelStream()
                    .forEach(d -> parseFile(progress, d));
        
        return null;
    }

    public Map<SnapShotHeader, Map<Long, ObjectData>> getSnapShots() {
        return snapShots;
    }

}
