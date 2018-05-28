package elevatorapp;

import org.joda.time.LocalDate;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        schema = "todo",
        table = "Elevator",
        identityType= IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Uniques({
        @javax.jdo.annotations.Unique(
                name="Elevator_name_must_be_unique",
                members={"name"})
})

@DomainObject(auditing = Auditing.ENABLED)
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor

public class Elevator implements Comparable<Elevator> {

    @Column(allowsNull = "false", length = 30)
    @NonNull
    @Property
    @Setter
    @Getter
    @Title
    private String name;

    @Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED)
    public Elevator updateName(
            @Parameter(maxLength = 30)
            @ParameterLayout(named = "Name")
            final String name) {
        setName(name);
        return this;
    }

    public String default0UpdateName() {
        return getName();
    }

    public TranslatableString validate0UpdateName(final String name) {
        return name != null && name.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.remove(this);
    }

    // 上一次维护日期
    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Setter
    @Getter
    private LocalDate lastMaintainDate;

    // 下一次维护日期
    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Setter
    @Getter
    private LocalDate nextMaintainDate;

    @Column(allowsNull = "true")
    @Property(editing = Editing.ENABLED)
    @Setter
    @Getter
    private String description;

    // 维护，更新上一次维护日期，确定下一次维护日期，并创建一个todo
    public void Maintain(){

    }

    @Override
    public int compareTo(final Elevator o) {
        return 0;
    }

    //region > injected services
    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    @lombok.Getter(AccessLevel.NONE) @lombok.Setter(AccessLevel.NONE)
    MessageService messageService;
    //endregion
}
