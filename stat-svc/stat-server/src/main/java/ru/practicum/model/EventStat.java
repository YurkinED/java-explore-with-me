package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(schema = "public", name = "hits")
public class EventStat {
    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "events_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime created;

}
