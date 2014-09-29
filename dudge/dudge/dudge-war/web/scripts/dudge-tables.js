/* Default class modification */
$.extend( $.fn.dataTableExt.oStdClasses, {
    "sWrapper": "dataTables_wrapper form-inline"
} );

$.fn.dataTableExt.oApi.dudgeTableStyles = function ( oSettings ) {
    $('#' + oSettings.sTableId + '_length label select').addClass('form-control');
    $('#' + oSettings.sTableId + '_filter label input').addClass('form-control');   
};

$.fn.dataTable.models.oSettings['aoInitComplete'].push( {
    "fn": $.fn.dataTableExt.oApi.dudgeTableStyles,
    "sName": 'dudgeTableStyles'
} );
    
/* API method to get paging information */
 $.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
 {
     return {
         "iStart":         oSettings._iDisplayStart,
         "iEnd":           oSettings.fnDisplayEnd(),
         "iLength":        oSettings._iDisplayLength,
         "iTotal":         oSettings.fnRecordsTotal(),
         "iFilteredTotal": oSettings.fnRecordsDisplay(),
         "iPage":          oSettings._iDisplayLength === -1 ?
             0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
         "iTotalPages":    oSettings._iDisplayLength === -1 ?
             0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
     };
 };

 /* Bootstrap style pagination control */
 $.extend( $.fn.dataTableExt.oPagination, {
     "bootstrap": {
         "fnInit": function( oSettings, nPaging, fnDraw ) {
             var fnClickHandler = function ( e ) {
                 e.preventDefault();
                 if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
                     fnDraw( oSettings );
                 }
             };

             $(nPaging).append(
                 '<ul class="pagination dudge-pagination">'+
                     '<li class="prev disabled"><a href="#">&larr; </a></li>'+
                     '<li class="next disabled"><a href="#">&rarr; </a></li>'+
                 '</ul>'
             );
             var els = $('a', nPaging);
             $(els[0]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
             $(els[1]).bind( 'click.DT', { action: "next" }, fnClickHandler );
         },

         "fnUpdate": function ( oSettings, fnDraw ) {
             var iListLength = 7;
             var oPaging = oSettings.oInstance.fnPagingInfo();
             var an = oSettings.aanFeatures.p;
             var i, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);

             if ( oPaging.iTotalPages <= iListLength) {
                 iStart = 1;
                 iEnd = oPaging.iTotalPages;
             }
             else if ( oPaging.iPage <= iHalf ) {
                 iStart = 1;
                 iEnd = iListLength - 2;
             } else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf - 1) ) {
                 iStart = oPaging.iTotalPages - iListLength + 3;
                 iEnd = oPaging.iTotalPages;
             } else {
                 iStart = oPaging.iPage - iHalf + 3;
                 iEnd = oPaging.iPage + iHalf - 1;
             }

             for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
                 // Remove the middle elements
                 $('li:gt(0)', an[i]).filter(':not(:last)').remove();

                 if (iStart > 1) {
                     $('<li><a href="#">1</a></li>')
                         .insertBefore( $('li:last', an[i])[0] )
                         .bind('click', function (e) {
                             e.preventDefault();
                             oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
                             fnDraw( oSettings );
                         } );
                     $('<li class="disabled"><a href="#">&#133;</a></li>')
                         .insertBefore( $('li:last', an[i])[0] )
                         .click(function(e) {
                            e.preventDefault();;
                         });
                 }
                 
                 // Add the new list items and their event handlers
                 for ( j=iStart ; j<=iEnd ; j++ ) {
                     sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
                     $('<li '+sClass+'><a href="#">'+j+'</a></li>')
                         .insertBefore( $('li:last', an[i])[0] )
                         .bind('click', function (e) {
                             e.preventDefault();
                             oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
                             fnDraw( oSettings );
                         } );
                 }
                 
                 if (iEnd < oPaging.iTotalPages) {
                     $('<li class="disabled"><a href="#">&#133;</a></li>')
                         .insertBefore( $('li:last', an[i])[0] )
                         .click(function(e) {
                            e.preventDefault();;
                         });
                     $('<li><a href="#">' + oPaging.iTotalPages + '</a></li>')
                         .insertBefore( $('li:last', an[i])[0] )
                         .bind('click', function (e) {
                             e.preventDefault();
                             oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
                             fnDraw( oSettings );
                         } );
                 }

                 // Add / remove disabled classes from the static elements
                 if ( oPaging.iPage === 0 ) {
                     $('li:first', an[i]).addClass('disabled');
                 } else {
                     $('li:first', an[i]).removeClass('disabled');
                 }

                 if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
                     $('li:last', an[i]).addClass('disabled');
                 } else {
                     $('li:last', an[i]).removeClass('disabled');
                 }
             }
         }
     }
 } );
 
$.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw )
{
    // DataTables 1.10 compatibility - if 1.10 then versionCheck exists.
    // 1.10s API has ajax reloading built in, so we use those abilities
    // directly.
    if ( $.fn.dataTable.versionCheck ) {
        var api = new $.fn.dataTable.Api( oSettings );
 
        if ( sNewSource ) {
            api.ajax.url( sNewSource ).load( fnCallback, !bStandingRedraw );
        }
        else {
            api.ajax.reload( fnCallback, !bStandingRedraw );
        }
        return;
    }
 
    if ( sNewSource !== undefined && sNewSource !== null ) {
        oSettings.sAjaxSource = sNewSource;
    }
 
    // Server-side processing should just call fnDraw
    if ( oSettings.oFeatures.bServerSide ) {
        this.fnDraw();
        return;
    }
 
    this.oApi._fnProcessingDisplay( oSettings, true );
    var that = this;
    var iStart = oSettings._iDisplayStart;
    var aData = [];
 
    this.oApi._fnServerParams( oSettings, aData );
 
    oSettings.fnServerData.call( oSettings.oInstance, oSettings.sAjaxSource, aData, function(json) {
        /* Clear the old information from the table */
        that.oApi._fnClearTable( oSettings );
 
        /* Got the data - add it to the table */
        var aData =  (oSettings.sAjaxDataProp !== "") ?
            that.oApi._fnGetObjectDataFn( oSettings.sAjaxDataProp )( json ) : json;
 
        for ( var i=0 ; i<aData.length ; i++ )
        {
            that.oApi._fnAddData( oSettings, aData[i] );
        }
         
        oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
 
        that.fnDraw();
 
        if ( bStandingRedraw === true )
        {
            oSettings._iDisplayStart = iStart;
            that.oApi._fnCalculateEnd( oSettings );
            that.fnDraw( false );
        }
 
        that.oApi._fnProcessingDisplay( oSettings, false );
 
        /* Callback user function - for event handlers etc */
        if ( typeof fnCallback === 'function' && fnCallback !== null )
        {
            fnCallback( oSettings );
        }
    }, oSettings );
};