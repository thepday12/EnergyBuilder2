package edv2.energybuilder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import edv2.energybuilder.model.ValueAndDate;

import static edv2.energybuilder.utils.MyUtils.formatDate;

public class StringDateComparator implements Comparator<ValueAndDate>
{
    SimpleDateFormat dateFormat = new SimpleDateFormat(formatDate);


    @Override
    public int compare(ValueAndDate lhs, ValueAndDate rhs)
    {
        try {
            return dateFormat.parse(lhs.getDate()).compareTo(dateFormat.parse(rhs.getDate()));
        } catch (ParseException e) {
            return 0;
        }
    }


}
