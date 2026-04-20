package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserAuthService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);
    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;

    public boolean isAuthorizedByTeam(User user, UUID teamId, RoleEnum minimumRole) {
        Optional<Club> club = clubRepository.findClubByTeam(teamId);
        if (club.isPresent()) {
            RoleEnum clubRole = findClubUserRole(user, club.get().getId());
            if (clubRole != null && RoleEnum.isEqualsOrHigher(clubRole, minimumRole)) {
                return true;
            }
        }
        RoleEnum teamRole = findTeamUserRole(user, teamId);
        if (teamRole != null && RoleEnum.isEqualsOrHigher(teamRole, minimumRole)) {
            return true;
        }
        return false;
    }

    public boolean isAuthorizedByClub(User user, UUID clubId, RoleEnum minimumRole) {
        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isPresent()) {
            RoleEnum clubRole = findClubUserRole(user, club.get().getId());
            if (clubRole != null && RoleEnum.isEqualsOrHigher(minimumRole, clubRole)) {
                return true;
            }
        }
        return false;
    }

    public RoleEnum findTeamUserRole(User user, UUID teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isPresent()) {
            Optional<Club> club = clubRepository.findClubByTeam(teamId);
            if (club.isPresent()) {
                RoleEnum role = findClubUserRole(user, club.get().getId());
                if (role != null) {
                    return role;
                }
            }

            if (team.get().getTrainer() != null && team.get().getTrainer().equals(user.getId())) {
                return logAndReturnRole(user, team.get(), club.get(), RoleEnum.TRAINER);
            }

            if (team.get().getSecondTrainer() != null && team.get().getSecondTrainer().equals(user.getId())) {
                return logAndReturnRole(user, team.get(), club.get(), RoleEnum.SECOND_TRAINER);
            }

            if (team.get().getScouters() != null && team.get().getScouters().stream().anyMatch(s -> s.equals(user.getId()))) {
                return logAndReturnRole(user, team.get(), club.get(), RoleEnum.SCOUTER);
            }
        }
        return null;
    }

    public RoleEnum findClubUserRole(User user, UUID clubId) {
        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isPresent()) {
            boolean clubHasUser = club.get().getAdminUserIds()
                    .stream()
                    .anyMatch(t -> t.equals(user.getId()));
            if (clubHasUser) {
                return logAndReturnRole(user, club.get(), RoleEnum.ADMIN);
            }
        }
        return null;
    }

    private RoleEnum logAndReturnRole(User user, Team team, Club club, RoleEnum role) {
        logger.info("[AUTH] User '{}' has role {} in team {}, of club '{}'", user.getUsername(), role, team.getName(), club.getName());
        return role;
    }

    private RoleEnum logAndReturnRole(User user, Club club, RoleEnum role) {
        logger.info("[AUTH] User '{}' has role {} in club {}", user.getUsername(), role, club.getName());
        return role;
    }
}
