package szalaimihaly.hu.ertidataviewer.dataloader;

/**
 * Created by szala on 2016. 04. 09..
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;

import szalaimihaly.hu.ertidataviewer.entities.*;

public abstract class AbstractDataLoader implements DataLoader  {

    protected ArrayList<ObservedObject> createNA(int beginyear, int beginmonth, int beginday, int endyear,
                                                 int endmonth, int endday, String species, String city, ArrayList<ObservedObject> its,
                                                 String aggregationType){
        switch (aggregationType) {
            case DataLoader.DAY: {
                for (int year = beginyear; year <= endyear; year++) {
                    if (year == beginyear && beginyear != endyear) {
                        createNAYear(year, beginmonth, beginday, 12, 31, its,
                                species, city);
                    }
                    if (year != beginyear && year != endyear) {
                        createNAYear(year, 1, 1, 12, 31, its, species, city);
                    }
                    if (year == endyear && beginyear != endyear) {
                        createNAYear(year, 1, 1, endmonth, endday, its, species,
                                city);
                    }
                    if (beginyear == endyear) {
                        createNAYear(beginyear, beginmonth, beginday, endmonth,
                                endday, its, species, city);
                    }
                }
                Collections.sort(its);
                return its;
            }
            case DataLoader.MONTH: {
                ArrayList<ObservedObject> list = new ArrayList<ObservedObject>();
                ArrayList<ObservedObject> itscopy = (ArrayList<ObservedObject>) copy(its);
                InsectTrap key;
                if (its.size() > 0) {
                    key = (InsectTrap) itscopy.get(0);
                } else {
                    key = new InsectTrap(null, species, 0, beginyear, beginmonth, beginday, city, 0);
                    key.setNA();
                }
                for (ObservedObject observedObject : itscopy) {
                    InsectTrap insectTrap = (InsectTrap) observedObject;
                    if (key.getMonth() == insectTrap.getMonth() && !key.equals(insectTrap)) {
                        key.setSpeciesId(insectTrap.getSpeciesId());
                        key.setSpeciesName(species);
                        key.setCity(city);
                        key.setYear(insectTrap.getYear());
                        key.setMonth(insectTrap.getMonth());
                        key.setDay(1);
                        int catches = key.getCatches();
                        key.setCatches(catches + insectTrap.getCatches());
                    } else {
                        if(!list.contains(key)){
                            list.add(key);
                        }
                        key = insectTrap;
                    }
                }
                if (!list.contains(key)) {
                    list.add(key);
                }
                for (int year = beginyear; year <= endyear; year++) {
                    int begin = 0;
                    int end = 0;
                    if (year == beginyear && beginyear != endyear) {
                        begin = beginmonth;
                        end = 12;
                    }
                    if (year != beginyear && year != endyear) {
                        begin = 1;
                        end = 12;
                    }
                    if (year == endyear && beginyear != endyear) {
                        begin = 1;
                        end = endmonth;
                    }
                    if (beginyear == endyear) {
                        begin = beginmonth;
                        end = endmonth;
                    }
                    for (int month = begin; month <= end; month++) {
                        createNAMonth(year, month, 1, 1, list, city, species);
                    }
                }
                Collections.sort(list);
                return list;
            }
            default:
                break;
        }
        return null;
    }

    protected void createNAMonth(int year, int month, int beginday, int endday,
                                 ArrayList<ObservedObject> observedobjects, String city,
                                 String species) {
        int maxday;
        if (endday >= 31) {
            maxday = 31;
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                maxday = 30;
            }
            if (month == 2) {
                maxday = 28;
                if (new GregorianCalendar().isLeapYear(year)) {
                    maxday = 29;
                }
            }
        } else {
            maxday = endday;
        }
        int minday = beginday;
        for (int day = minday; day <= maxday; day++) {
            String dateString = makeDateSring(year, month, day);
            boolean found = false;
            for (ObservedObject observedObject : observedobjects) {
                if (observedObject.getDateString().equals(dateString)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                InsectTrap insectTrap = new InsectTrap(null, species, 0, year,
                        month, day, city, 0);
                insectTrap.setNA();
                observedobjects.add(insectTrap);
            }
        }
    }

    protected void createNAYear(int year, int beginmonth, int beginday,
                                int endmonth, int endday,
                                ArrayList<ObservedObject> observedObjects, String species,
                                String city) {
        for (int month = beginmonth; month <= endmonth; month++) {
            if (month == beginmonth && beginmonth != endmonth) {
                createNAMonth(year, month, beginday, 31, observedObjects, city,
                        species);
            }
            if (month != beginmonth && month != endmonth
                    && beginmonth != endmonth) {
                createNAMonth(year, month, 1, 31, observedObjects, city,
                        species);
            }
            if (month == endmonth && beginmonth != endmonth) {
                createNAMonth(year, month, 1, endday, observedObjects, city,
                        species);
            }
            if (beginmonth == endmonth) {
                createNAMonth(year, month, beginday, endday, observedObjects,
                        city, species);
            }
        }
    }

    protected Object copy(Object original) {
        Object object = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(original);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            object = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }

    protected String makeDateSring(int year, int month, int day) {
        String dateString;
        if (day >= 10) {
            if (month >= 10) {
                dateString = year + "-" + month + "-" + day;
            } else {
                dateString = year + "-0" + month + "-" + day;
            }
        } else if (month >= 10) {
            dateString = year + "-" + month + "-0" + day;
        } else {
            dateString = year + "-0" + month + "-0" + day;
        }
        return dateString;
    }
}
