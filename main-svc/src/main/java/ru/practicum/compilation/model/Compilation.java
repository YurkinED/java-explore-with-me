package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "compilations", schema = "public")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id")
    private Collection<Event> events;
    @Column(name = "pinned")
    private boolean pinned;
    @Column(name = "title")
    private String title;

}
