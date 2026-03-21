package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.List;
import java.util.stream.Collectors;

public class StreamingMonitor {
    public int nowIdIndex = 0;
    public int cntTicks;
    public int cntCompleteTicks = 0;
    public List<Integer> streamIds;

    StreamingMonitor(List<StreamWriter> streamIds, int cntTicks) {
        this.cntTicks = cntTicks;
        this.streamIds = streamIds
                .stream()
                .map(writer -> writer.getId())
                .sorted()
                .collect(Collectors.toList());
    }

    void nextId() {
        nowIdIndex++;
        if (nowIdIndex == streamIds.size()) {
            cntCompleteTicks++;
            nowIdIndex = 0;
        }
    }
}
