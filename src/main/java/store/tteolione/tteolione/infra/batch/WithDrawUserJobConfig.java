//package store.tteolione.tteolione.infra.batch;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.PlatformTransactionManager;
//import store.tteolione.tteolione.domain.product.entity.Product;
//import store.tteolione.tteolione.domain.user.entity.User;
//import store.tteolione.tteolione.domain.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//@RequiredArgsConstructor
//public class WithDrawUserJobConfig {
//
//    private final int CHUNK_SIZE = 20;
//    private final UserRepository userRepository;
//
//    @Bean
//    public Job withDrawUserJob(JobRepository jobRepository, Step withDrawUserStep1) {
//        return new JobBuilder("withDrawUserJob", jobRepository)
//                .start(withDrawUserStep1)
//                .incrementer(new RunIdIncrementer())
//                .build();
//    }
//
//    @JobScope
//    @Bean
//    public Step withDrawUserStep1(
//            JobRepository jobRepository,
//            ItemReader<User> step1Reader,
//            ItemProcessor<User, User> step1Processor,
//            ItemWriter<User> step1Writer,
//            PlatformTransactionManager platformTransactionManager
//    ) {
//        return new StepBuilder("withDrawUserStep1", jobRepository)
//                .<User, User>chunk(CHUNK_SIZE, platformTransactionManager)
//                .reader(step1Reader)
//                .processor(step1Processor)
//                .writer(step1Writer)
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public ItemReader<User> step1Reader(
//            @Value("#{jobParameters['updateDate']}") String _updateDate
//    ) {
//        LocalDateTime updateDate = parse(_updateDate);
//
//        return new RepositoryItemReaderBuilder<User>()
//                .name("step1Reader")
//                .repository(userRepository)
//                .methodName("findByUserId")
//                .pageSize(CHUNK_SIZE)
//                .arguments(Arrays.asList(1L, updateDate))
//                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public ItemProcessor<User, User> step1Processor() {
//        return User::toRoleWithDrawUserAuthority;
//    }
//
//    @StepScope
//    @Bean
//    public ItemWriter<User> step1Writer() {
//        return users -> users.forEach(user -> {
//                userRepository.save(user);
//        });
//    }
//
//    public LocalDateTime parse(String dateText) {
//        String pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS";
//        return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern(pattern));
//    }
//
//}
