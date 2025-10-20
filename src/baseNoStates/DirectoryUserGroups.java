package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class DirectoryUserGroups {
    private DirectoryUserGroups() {}

    private static final List<UserGroup> userGroups = new ArrayList<>();

    public static void makeUserGroups() {
        userGroups.clear();

        // Accions per grup
        ArrayList<String> adminActions = new ArrayList<>();
        adminActions.add("open");
        adminActions.add("close");
        adminActions.add("lock");
        adminActions.add("unlock");
        adminActions.add("unlock_shortly");

        ArrayList<String> managerActions = new ArrayList<>();
        managerActions.add("open");
        managerActions.add("close");
        managerActions.add("lock");
        managerActions.add("unlock");
        managerActions.add("unlock_shortly");

        ArrayList<String> employeeActions = new ArrayList<>();
        employeeActions.add("open");
        employeeActions.add("close");
        employeeActions.add("unlock_shortly");

        // ===== Àrees =====
        // Admin: totes les àrees rellevants (hi incloem les 3 stairs)
        ArrayList<Area> allAreas = new ArrayList<>();
        addAreaIfExists(allAreas, "parking");
        addAreaIfExists(allAreas, "hall");
        addAreaIfExists(allAreas, "room1");
        addAreaIfExists(allAreas, "room2");
        addAreaIfExists(allAreas, "room3");
        addAreaIfExists(allAreas, "corridor");
        addAreaIfExists(allAreas, "IT");
        addAreaIfExists(allAreas, "exterior");
        // ESCALERES (els tres espais d'escales)
        addAreaIfExists(allAreas, "stairs_basement");
        addAreaIfExists(allAreas, "stairs_ground");
        addAreaIfExists(allAreas, "stairs_floor1");

        // Managers: el mateix conjunt que admin
        ArrayList<Area> managerAreas = new ArrayList<>(allAreas);

        // Employees: tot EXCEPTE parking, però SÍ incloem ESCALERES
        ArrayList<Area> employeeAreas = new ArrayList<>();
        addAreaIfExists(employeeAreas, "hall");
        addAreaIfExists(employeeAreas, "room1");
        addAreaIfExists(employeeAreas, "room2");
        addAreaIfExists(employeeAreas, "room3");
        addAreaIfExists(employeeAreas, "corridor");
        addAreaIfExists(employeeAreas, "IT");
        addAreaIfExists(employeeAreas, "exterior");
        addAreaIfExists(employeeAreas, "stairs_basement");
        addAreaIfExists(employeeAreas, "stairs_ground");
        addAreaIfExists(employeeAreas, "stairs_floor1");
        // (no afegim "parking" per a employees)

        // ===== Horaris =====
        LocalDate today = LocalDate.now();

        // Admin: sempre (0:00–23:59 tots els dies, any actual → 2100)
        LocalDate adminStart = LocalDate.of(today.getYear(), 1, 1);
        LocalDate adminEnd   = LocalDate.of(2100, 1, 1);
        ArrayList<DayOfWeek> allDays = new ArrayList<>();
        allDays.add(DayOfWeek.MONDAY);
        allDays.add(DayOfWeek.TUESDAY);
        allDays.add(DayOfWeek.WEDNESDAY);
        allDays.add(DayOfWeek.THURSDAY);
        allDays.add(DayOfWeek.FRIDAY);
        allDays.add(DayOfWeek.SATURDAY);
        allDays.add(DayOfWeek.SUNDAY);
        Schedule adminSchedule = new Schedule(adminStart, adminEnd, allDays,
                LocalTime.of(0, 0), LocalTime.of(23, 59));

        // Managers: 1 Sep any actual → 1 Mar any següent, dll–ds 8–20
        LocalDate managerStart = LocalDate.of(today.getYear(), 9, 1);
        LocalDate managerEnd   = LocalDate.of(today.getYear() + 1, 3, 1);
        ArrayList<DayOfWeek> managerDays = new ArrayList<>();
        managerDays.add(DayOfWeek.MONDAY);
        managerDays.add(DayOfWeek.TUESDAY);
        managerDays.add(DayOfWeek.WEDNESDAY);
        managerDays.add(DayOfWeek.THURSDAY);
        managerDays.add(DayOfWeek.FRIDAY);
        managerDays.add(DayOfWeek.SATURDAY);
        Schedule managerSchedule = new Schedule(managerStart, managerEnd, managerDays,
                LocalTime.of(8, 0), LocalTime.of(20, 0));

        // Employees: 1 Sep any actual → 1 Mar any següent, dll–dv 9–17
        LocalDate employeeStart = LocalDate.of(today.getYear(), 9, 1);
        LocalDate employeeEnd   = LocalDate.of(today.getYear() + 1, 3, 1);
        ArrayList<DayOfWeek> employeeDays = new ArrayList<>();
        employeeDays.add(DayOfWeek.MONDAY);
        employeeDays.add(DayOfWeek.TUESDAY);
        employeeDays.add(DayOfWeek.WEDNESDAY);
        employeeDays.add(DayOfWeek.THURSDAY);
        employeeDays.add(DayOfWeek.FRIDAY);
        Schedule employeeSchedule = new Schedule(employeeStart, employeeEnd,
                employeeDays, LocalTime.of(9, 0), LocalTime.of(17, 0));

        // ===== Grups =====
        UserGroup adminGroup    = new UserGroup("Administrators", adminActions,    allAreas,     adminSchedule);
        UserGroup managerGroup  = new UserGroup("Managers",       managerActions,  managerAreas, managerSchedule);
        UserGroup employeeGroup = new UserGroup("Employees",      employeeActions, employeeAreas, employeeSchedule);

        // ===== Usuaris (exemples) =====
        // Admins
        User ana = new User("Ana", "11343", adminGroup);
        adminGroup.addUser(ana);

        // Managers
        User manel = new User("Manel", "95783", managerGroup);
        User marta = new User("Marta", "05827", managerGroup);
        managerGroup.addUser(manel);
        managerGroup.addUser(marta);

        // Employees
        User ernest  = new User("Ernest",  "74984", employeeGroup);
        User eulalia = new User("Eulalia", "43295", employeeGroup);
        employeeGroup.addUser(ernest);
        employeeGroup.addUser(eulalia);

        // Blank (sense permisos)
        ArrayList<String> noActions = new ArrayList<>();
        ArrayList<Area> noAreas = new ArrayList<>();
        Schedule noSchedule = new Schedule(today, today.minusDays(1), new ArrayList<>(),
                LocalTime.of(0, 0), LocalTime.of(0, 0));
        UserGroup blankGroup = new UserGroup("Blank", noActions, noAreas, noSchedule);
        User bernat = new User("Bernat", "12345", blankGroup);
        User blai   = new User("Blai",   "77532", blankGroup);
        blankGroup.addUser(bernat);
        blankGroup.addUser(blai);

        userGroups.add(blankGroup);
        userGroups.add(employeeGroup);
        userGroups.add(managerGroup);
        userGroups.add(adminGroup);
    }

    public static User findUserByCredential(String credential) {
        for (UserGroup group : userGroups) {
            for (User user : group.getUsers()) {
                if (user.getCredential().equals(credential)) return user;
            }
        }
        return null;
    }

    private static void addAreaIfExists(ArrayList<Area> areaList, String areaId) {
        Area a = DirectoryAreas.findAreaById(areaId);
        if (a != null) areaList.add(a);
    }
}
