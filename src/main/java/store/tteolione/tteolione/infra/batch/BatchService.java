package store.tteolione.tteolione.infra.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobLauncher jobLauncher;
    private final Job withDrawUserJob;

    public void runWithDrawUserJob(LocalDateTime _updateDate) {
        try {
            String updateDate = _updateDate.toString().substring(0, 10) + " 00:00:00.000000"; //배포용
//            String updateDate = _updateDate.toString().substring(0, 10) + " " +_updateDate.toString().substring(11); //테스트용

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("updateDate", updateDate)
                    .toJobParameters();
            jobLauncher.run(withDrawUserJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
