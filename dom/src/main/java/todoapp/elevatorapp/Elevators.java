package todoapp.elevatorapp;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Elevators",
        menuOrder = "11"
)
public class Elevators {
//
//    @ActionLayout(cssClassFa = "fa fa-plus")
//    @MemberOrder(sequence = "5")
//    public Elevator newElevator(
//            @Parameter(regexPattern = "\\w[@&:\\-\\,\\.\\+ \\w]*")
//            final String name){
//        final Elevator ele = container.newTransientInstance(Elevator.class);
//        ele.setName(name);
//
//        return ele;
//    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Elevator> listAll() {
        return repositoryService.allInstances(Elevator.class);
    }


    public static class CreateDomainEvent extends ActionDomainEvent<Elevators> {}
    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "2")
    public Elevator create(
            @ParameterLayout(named="Name")
            final String name) {
        Elevator ae = new Elevator(name);
        return ae;
        //return repositoryService.persist(new Elevator(name));
    }


    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    IsisJdoSupport isisJdoSupport;

    @javax.inject.Inject
    private DomainObjectContainer container;
}
