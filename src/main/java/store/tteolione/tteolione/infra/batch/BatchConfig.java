package store.tteolione.tteolione.infra.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BatchConfig {

    private final BatchService batchService;

    @Scheduled(cron = "0 0 6 * * *") // 운영
//    @Scheduled(cron = "0 * * * * *") // 테스트
    public void runWithDrawUserJob() {
        LocalDateTime updateDate = LocalDateTime
                .now()
//                .minusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        batchService.runWithDrawUserJob(updateDate);
    }
}
