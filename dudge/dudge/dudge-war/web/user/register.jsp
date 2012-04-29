<script type="text/javascript">
Ext.onReady(function(){

//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

var form = new Ext.form.FormPanel({
    monitorValid:true,
    renderTo: 'register-form',
    labelWidth: 75,
    title: '<bean:message key="registration.registration" />',
    bodyStyle: 'padding:15px',
    autoWidth: 'true',
    labelPad: 20,
    layoutConfig: {
        labelSeparator: ''
    },
    defaults: {
        width: 230,
        msgTarget: 'side'
    },
    defaultType: 'textfield',
    baseParams: { reqCode: 'submitRegister' },
    items: [{
                    id: 'login',
                    fieldLabel: '<bean:message key="user.login" />',
                    allowBlank: false

            },
            {
                    id: 'password',
                    fieldLabel: '<bean:message key="user.password" />',
                    inputType: 'password',
                    allowBlank: false
            }/*,
            {
                    id:"from",
                    fieldLabel:"From",
                    width:275,
                    allowBlank:false,
                    blankText:"Please enter a from address",
                    vtype:"email",
                    vtypeText:"The from field should be an email address in the format of user@domain.com"
            },
            {
                    id:"to",
                    fieldLabel:"To",
                    width:275,
                    allowBlank:false,
                    blankText:"Please enter a to address"
            },
            {
                    id:"subject",
                    fieldLabel:"Subject",
                    width:275,
                    allowBlank:false,
                    blankText:"Please enter a subject address"
            },
            new Ext.form.TextArea({
                    id:"message",
                    fieldLabel:"Message",
                    width:275,
                    height:100
            })*/
	],
	buttons:[{
                    text: '<bean:message key="registration.register" />',
                    type: 'submit',
                    formBind: true,
                    handler: function()
                    {
                        form.getForm().get('login').get
                        form.getForm().submit();
                    }
	 }
	]
});

}); // Ext.OnReady()
</script>
<div id="register-form" />
