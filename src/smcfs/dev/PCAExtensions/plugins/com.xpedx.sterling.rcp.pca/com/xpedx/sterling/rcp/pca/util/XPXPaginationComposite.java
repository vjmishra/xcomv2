/**
 * 
 */
package com.xpedx.sterling.rcp.pca.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Widget;

import com.yantra.yfc.rcp.YRCConstants;
import com.yantra.yfc.rcp.YRCPlatformUI;

/**
 * @author Krithika S
 *
 */
public abstract class XPXPaginationComposite extends Composite{
	
	
	public XPXPaginationComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	//The pagination links or controls.
	protected Link lnkPrevious;
	protected Link lnkNext;
    protected Link lnkGetNextPageRecords;
    
    /**
     * This method is called from the screen class to create the pagination panel with links/controls
     * depending on the pagination strategy.
     * If the strategy is NEXTPAGE, only one link for fetching the next page of records will be created.
     * If the strategy is GENERIC, two links, one for viewing the previous page of records and one to view the next page of records will be created
     * @param parentComposite
     * @param paginationStrategy
     */
    protected void createPaginationLinks(Composite parentComposite, String paginationStrategy)
    {
    	
    	if("NEXTPAGE".equals(paginationStrategy))
    	{
    		//Create Get Next Page Link
    		lnkGetNextPageRecords = new Link(parentComposite, SWT.NONE);
    		lnkGetNextPageRecords.setData("name","lnkGetNextPageRecords");
    		GridData lnkNextlayoutData = new GridData();
    		lnkNextlayoutData.horizontalAlignment = SWT.END;
    	
    		lnkGetNextPageRecords.setLayoutData(lnkNextlayoutData);
    		lnkGetNextPageRecords.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Link");
    		lnkGetNextPageRecords.setText("Get Next Page");
    		lnkGetNextPageRecords.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
					String previousPageNumber = "1";
					int currentPageNo = Integer.parseInt(getPaginationBehavior().getXpxPaginationData().getPageNumber());
					if(currentPageNo>=1 )
					{
						previousPageNumber = Integer.toString(currentPageNo);
					}
					getPaginationBehavior().getXpxPaginationData().setPageNumber(previousPageNumber);
					getPaginationBehavior().search();
					}
				}
			);
			
    		lnkGetNextPageRecords.setVisible(false);
    	}
    	else if("GENERIC".equals(paginationStrategy)) {
    		//Create Previous and Next Links
			lnkPrevious = new Link(parentComposite, SWT.BEGINNING);
			lnkPrevious.setData("name","lnkPrevious");
			GridData lnkPreviouslayoutData = new GridData();
			lnkPreviouslayoutData.horizontalAlignment = SWT.END;
		
			lnkPrevious.setLayoutData(lnkPreviouslayoutData);
			lnkPrevious.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Link");
			lnkPrevious.setText("Previous");
			
			lnkPrevious.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
					String pageNumberToBeFetched = "1";
					int currentPageNo = Integer.parseInt(getPaginationBehavior().getXpxPaginationData().getPageNumber());
					if(currentPageNo>1 )
					{
						pageNumberToBeFetched = Integer.toString(currentPageNo - 1);
					}
					getPaginationBehavior().getXpxPaginationData().setPageNumber(pageNumberToBeFetched);
					getPaginationBehavior().getXpxPaginationData().setPreviousPageElem(null);
					getPaginationBehavior().search();
					}
				}
			);
			lnkPrevious.setVisible(false);
			
			lnkNext = new Link(parentComposite, SWT.NONE);
			lnkNext.setData("name","lnkNext");
			GridData lnkNextlayoutData = new GridData();
			lnkNextlayoutData.horizontalAlignment = SWT.END;
		
			lnkNext.setLayoutData(lnkNextlayoutData);
			lnkNext.setData(YRCConstants.YRC_CONTROL_CUSTOMTYPE, "Link");
			lnkNext.setText("Next");
			
			lnkNext.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
				public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
					String pageNumberToBeFetched = "1";
					int currentPageNo = Integer.parseInt(getPaginationBehavior().getXpxPaginationData().getPageNumber());
					int totalNumOfPages = Integer.parseInt(getPaginationBehavior().getXpxPaginationData().getTotalNoOfPages());
					if((currentPageNo + 1) <=totalNumOfPages)
						pageNumberToBeFetched = Integer.toString(currentPageNo + 1);
					
					getPaginationBehavior().getXpxPaginationData().setPageNumber(pageNumberToBeFetched);
					getPaginationBehavior().getXpxPaginationData().setPreviousPageElem(null);
					getPaginationBehavior().search();
					}
				}
			);
			
			lnkNext.setVisible(false);
    	}
    }
    
    /**
     * This method is used to fetch the pagination controls/links specific to the pagination strategy.
     * @param paginationStrategy
     * @return
     */
    public Link[] getPaginationLinkControls(String paginationStrategy)
    {
    	if(getPaginationBehavior().NEXTPAGE_PAGINATION_STRATEGY.equals(paginationStrategy)){
    		return new Link[]{getLnkGetNextPageRecords()};
    	}
    	else if(getPaginationBehavior().GENERIC_PAGINATION_STRATEGY.equals(paginationStrategy)) {
    		return new Link[]{getLnkPrevious(), getLnkNext()};
    	}
		return null;
    }
    
    /**
     * This method is used to fetch all the pagination controls/links
     */
    public Link[] getAllPaginationLinkControls()
    {
    	return new Link[] { getLnkPrevious(), getLnkNext(),
				getLnkGetNextPageRecords() };
    }


	public Link getLnkPrevious() {
		return lnkPrevious;
	}

	public void setLnkPrevious(Link lnkPrevious) {
		this.lnkPrevious = lnkPrevious;
	}

	public Link getLnkNext() {
		return lnkNext;
	}

	public void setLnkNext(Link lnkNext) {
		this.lnkNext = lnkNext;
	}

	public Link getLnkGetNextPageRecords() {
		return lnkGetNextPageRecords;
	}

	public void setLnkGetNextPageRecords(Link lnkGetNextPageRecords) {
		this.lnkGetNextPageRecords = lnkGetNextPageRecords;
	}
    
	//This method is made abstract so that each pagination composite screen can fetch its pagination behavior.
    public abstract XPXPaginationBehavior getPaginationBehavior();
    

}
