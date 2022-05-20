package id.holigo.services.holigoairlinesservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "airports")
public class Airport {

    @Id
    @Column(length = 3, columnDefinition = "varchar(4)", nullable = false)
    private String id;

    @Column(columnDefinition = "tinyint(1)", nullable = false)
    private Boolean isInternational;

    @Column(columnDefinition = "tinyint(1)", nullable = false)
    private Boolean isDomestic;

    @Column(length = 50, columnDefinition = "varchar(50)")
    private String city;

    @Column(length = 50, columnDefinition = "varchar(50)")
    private String country;

    @Column(length = 100, columnDefinition = "varchar(100)")
    private String ianaTimezone;

    @Column(length = 100, columnDefinition = "varchar(100)")
    private String windowsTimezone;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
