package ru.practicum.event.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvent is a Querydsl query type for Event
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = -299444598L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvent event = new QEvent("event");

    public final StringPath annotation = createString("annotation");

    public final ru.practicum.category.model.QCategory category;

    public final NumberPath<Long> compilationId = createNumber("compilationId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdOn = createDateTime("createdOn", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> eventDate = createDateTime("eventDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ru.practicum.users.model.QUser initiator;

    public final QLocation location;

    public final BooleanPath paid = createBoolean("paid");

    public final NumberPath<Long> participantLimit = createNumber("participantLimit", Long.class);

    public final DateTimePath<java.time.LocalDateTime> publishedOn = createDateTime("publishedOn", java.time.LocalDateTime.class);

    public final BooleanPath requestModeration = createBoolean("requestModeration");

    public final SetPath<ru.practicum.request.model.Request, ru.practicum.request.model.QRequest> requests = this.<ru.practicum.request.model.Request, ru.practicum.request.model.QRequest>createSet("requests", ru.practicum.request.model.Request.class, ru.practicum.request.model.QRequest.class, PathInits.DIRECT2);

    public final EnumPath<TypeState> state = createEnum("state", TypeState.class);

    public final StringPath title = createString("title");

    public QEvent(String variable) {
        this(Event.class, forVariable(variable), INITS);
    }

    public QEvent(Path<? extends Event> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvent(PathMetadata metadata, PathInits inits) {
        this(Event.class, metadata, inits);
    }

    public QEvent(Class<? extends Event> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new ru.practicum.category.model.QCategory(forProperty("category")) : null;
        this.initiator = inits.isInitialized("initiator") ? new ru.practicum.users.model.QUser(forProperty("initiator")) : null;
        this.location = inits.isInitialized("location") ? new QLocation(forProperty("location")) : null;
    }

}

