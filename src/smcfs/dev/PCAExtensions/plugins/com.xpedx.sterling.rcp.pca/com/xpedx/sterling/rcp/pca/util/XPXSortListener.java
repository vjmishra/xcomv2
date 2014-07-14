package com.xpedx.sterling.rcp.pca.util;

import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class XPXSortListener implements Listener
{
    private Comparator currentComparator = null;
    
    private Collator col = Collator.getInstance(Locale.getDefault());
    
    public static final int INT_COMPARATOR    = 0;
    public static final int STRING_COMPARATOR = 1;
    public static final int DATE_COMPARATOR   = 2;
    
    
    private XPXSortListener(int _comp)
    {
        switch (_comp) 
        {
        case INT_COMPARATOR:
            currentComparator = intComparator;
            break;

        case STRING_COMPARATOR:
            currentComparator = strComparator;
            break;

        case DATE_COMPARATOR:
            currentComparator = dateComparator;
            break;
            
      
        default:
            currentComparator = strComparator;
        }
    }
    
    public static Listener getListener(int _comp)
    {
        return new XPXSortListener(_comp);
    }
    
    private int colIndex = 0;
    private int updown   = 1;
          
    // Integer Comparator
    private Comparator intComparator = new Comparator()
    {
        public int compare(Object arg0, Object arg1) {

            TableItem t1 = (TableItem)arg0;
            TableItem t2 = (TableItem)arg1;

            int v1 = Integer.parseInt(t1.getText(colIndex));
            int v2 = Integer.parseInt(t2.getText(colIndex));

            if (v1<v2) return 1*updown;
            if (v1>v2) return -1*updown;

            return 0;
        }    
    };
         
    // String Comparator
    private Comparator strComparator = new Comparator()
    {
        public int compare(Object arg0, Object arg1) {

            TableItem t1 = (TableItem)arg0;
            TableItem t2 = (TableItem)arg1;

            String v1 = (t1.getText(colIndex));
            String v2 = (t2.getText(colIndex));

            return (col.compare(v1,v2))*updown;
        }    
    };
    
   
    
    // Date Comparator

    private Comparator dateComparator = new Comparator()
    {
        public int compare(Object arg0, Object arg1) 
        {    
            TableItem t1 = (TableItem)arg0;
            TableItem t2 = (TableItem)arg1;
            
            String v1 = (t1.getText(colIndex)).trim();
            String v2 = (t2.getText(colIndex)).trim();

            v1.replaceAll("-", "/");
            v2.replaceAll("-", "/");
            
            DateFormat df_europe = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df_usa = new SimpleDateFormat("yyyy/MM/dd");

            DateFormat df = df_europe;
            
            Date d1 = null; Date d2 = null;
            
            try {
                d1 = df.parse(v1);
            } catch (ParseException e) 
            { 
               //System.out.println("[WARNING] v1 " + v1);
                d1 = new Date("01/01/1900");
            }
            
            try {               
                d2 = df.parse(v2);
            } catch (ParseException e) 
            { 
                d2 = new Date("01/01/1900");
            }

            if (d1.equals(d2))
                return 0;

            return updown*(d1.before(d2) ? 1 : -1);
        }    
    };
        
    
    
    private String[] getData(TableItem t)
    {
        Table table = t.getParent();
        
        int colCount = table.getColumnCount();
        String [] s = new String[colCount];
        
        for (int i = 0;i<colCount;i++)
            s[i] = t.getText(i);
                
        return s;
        
    }
    
    private int searchColumnIndex(TableColumn currentColumn)
    {
        Table table = currentColumn.getParent();
        
        int in = 0;
        
        for (int i = 0;i<table.getColumnCount();i++)
            if (table.getColumn(i) == currentColumn)
                in = i;
        
        return in;
    }

	public void handleEvent(Event e) {
		// TODO Auto-generated method stub
		 updown = (updown == 1 ? -1 : 1);
	        
	        TableColumn currentColumn = (TableColumn)e.widget;
	        Table table = currentColumn.getParent();
	    
	        colIndex = searchColumnIndex(currentColumn);
	        
	        table.setRedraw(false);
	        
	        TableItem[] items = table.getItems();
	       
	        Arrays.sort(items,currentComparator);
	        
	        table.setItemCount(items.length);
	        
	        for (int i = 0;i<items.length;i++)
	        {   
	            TableItem item = new TableItem(table,SWT.NONE,i);
	            item.setText(getData(items[i]));
	            items[i].dispose();
	        }
	        
	        table.setRedraw(true);   
		
	}
}

