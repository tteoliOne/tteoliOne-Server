package store.tteolione.tteolione.infra.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import store.tteolione.tteolione.domain.user.entity.User;
import store.tteolione.tteolione.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WithDrawUserJobConfig {

    private final int CHUNK_SIZE = 20;
    private final UserRepository userRepository;

    @Bean
    public Job withDrawUserJob(JobRepository jobRepository, Step withDrawUserStep1) {
        return new JobBuilder("withDrawUserJob", jobRepository)
                .start(withDrawUserStep1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @JobScope
    @Bean
    public Step withDrawUserStep1(
            JobRepository jobRepository,
            ItemReader<User> step1Reader,
            ItemProcessor<User, User> step1Processor,
            ItemWriter<User> step1Writer,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("withDrawUserStep1", jobRepository)
                .<User, User>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(step1Reader)
                .processor(step1Processor)
                .writer(step1Writer)
                .allowStartIfComplete(true)
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<User> step1Reader(
            @Value("#{jobParameters['updateDate']}") String _updateDate
    ) {
        LocalDateTime twoWeeksAgo = parse(_updateDate).minusWeeks(2);
        System.out.println("*************** step1Reader ***************"+ twoWeeksAgo);
        return new RepositoryItemReaderBuilder<User>()
                .name("step1Reader")
                .repository(userRepository)
                .methodName("findUsersWithWithdrawRoleAndOldUpdateAt")
                .pageSize(CHUNK_SIZE)
                .arguments(Arrays.asList(twoWeeksAgo))
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<User, User> step1Processor() {
        //유저 삭제 처리 - 이 부분을 적절한 처리 로직으로 채워 넣어야 함.
        return user -> {
            // 로직에 따라 user를 변환하거나 null을 반환하여 삭제 처리를 할 수 있음.
            System.out.println("*************** step1Processor ***************" + user.getUserId());
            return user;
        };
    }

    @StepScope
    @Bean
    public ItemWriter<User> step1Writer() {
        return new ItemWriter<User>() {
            @Override
            public void write(Chunk<? extends User> users) throws Exception {
                // 여기에서 사용자 삭제 로직을 구현합니다.
                List<User> userList = new ArrayList<>();
                for (User user : users) {
                    userList.add(user);
                }
                // userRepository의 삭제 메소드를 호출하여 한 번에 여러 사용자를 삭제할 수도 있습니다.
                userRepository.deleteUsersWithWithdrawRoleAndOldUpdateAt(userList);
            }
        };
    }

    public LocalDateTime parse(String dateText) {
        String pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS";
        return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern(pattern));
    }

}
