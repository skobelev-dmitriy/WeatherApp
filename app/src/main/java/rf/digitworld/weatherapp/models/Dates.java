package rf.digitworld.weatherapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Дмитрий on 25.11.2015.
 */
@Table(name = "Dates")
public class Dates extends Model {
    @Column(name="date")
    private String date;
    @Column(name = "Weather", onDelete = Column.ForeignKeyAction.CASCADE)
    private Weather weather;

    public Dates(){
        super();
    }

}
