package store.tteolione.tteolione.domain.user.entity;

import lombok.*;
import store.tteolione.tteolione.global.entity.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseTimeEntity {

    @Id
    @Column(name = "authority_name")
    private String authorityName;
}
