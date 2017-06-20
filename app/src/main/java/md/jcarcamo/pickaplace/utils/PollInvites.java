package md.jcarcamo.pickaplace.utils;

/**
 * Created by juan_ on 6/20/2017.
 */

public class PollInvites {
    private String title;
    private String id;
    private long position;

    public PollInvites() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        PollInvites guest = (PollInvites) obj;
        return id.equals(guest.id);
    }


}
