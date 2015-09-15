package charlyn23.c4q.nyc.projectx.stats;

/**
 * Created by July on 9/14/15.
 */
public class Bar {
    float perCent;
    String name;

    public Bar (String name, float perCent) {
        this.name = name;
        this.perCent = perCent;
    }

    public float getPerCent() {
        return perCent;
    }

    public void setPerCent(float perCent) {
        this.perCent = perCent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
