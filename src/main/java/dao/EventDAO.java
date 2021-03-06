package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Event;

/**
 * Interfaces with events in database
 */
public class EventDAO {
    /**
     * Connection to database
     */
    private Connection conn;

    /**
     * Create new EventDAO with given connection
     * @param conn connection to database
     */
    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert event into databse
     * @param event Event to insert
     * @throws DataAccessException on failure to insert (Invalid data or database failure)
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "insert into events (event_id, username, person_id, latitude, longitude, " +
                "country, city, event_type, year) values(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }

    /**
     * Find Event based on given eventID
     * @param eventID eventID to match
     * @return Found event, or null on event not found
     * @throws DataAccessException on database failure or invalid data
     */
    public Event getEventByID(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "select * from events where event_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("event_id"), rs.getString("username"),
                        rs.getString("person_id"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("event_type"),
                        rs.getInt("year"));
                return event;
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get all events associated with the given username
     * @param username Username of currently logged-in user, authenticated via AuthToken
     * @return array of events
     * @throws DataAccessException on database failure or invalid data
     */
    public Event[] getAllEventsByUsername(String username) throws DataAccessException {
        String countSql = "select count(*) from events where username = '" + username + "'";
        String sql = "select * from events where username = '" + username + "'";
        ResultSet rs;
        Event[] foundEvents;
        int numEvents;

        try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
            rs = countStmt.executeQuery();
            numEvents = rs.getInt(1);
        }
        catch (SQLException e) {
            throw new DataAccessException("Unable to count persons");
        }

        if (numEvents == 0) return null;
        else foundEvents = new Event[numEvents];

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                String event_id = rs.getString(1);
                String associatedUsername = rs.getString(2);
                String person_id = rs.getString(3);
                float latitude = rs.getFloat(4);
                float longitude = rs.getFloat(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);
                foundEvents[i] = new Event(event_id, associatedUsername, person_id, latitude,
                        longitude, country, city, eventType, year);
                i++;
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Error getting persons");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return foundEvents;
    }

    /**
     * Set the connection
     * @param conn New connection
     */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }
}
