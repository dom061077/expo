var registrationWizard = function() {  
    
	var cardsTotal = 4;
	
    var navHandler = function( direction ) {
       var lay = cardForm.getLayout();
       var i = lay.activeItem.id.split('card-')[1];
       var next = parseInt(i) + direction;
       lay.setActiveItem(next);
       Ext.getCmp('move-prev').setDisabled(next==0);
       Ext.getCmp('move-next').setDisabled(next==cardsTotal);
	   if(next >= cardsTotal) {
	   		Ext.getCmp('move-next').hide();
			Ext.getCmp('move-finish').show();
	   } else {
	   		Ext.getCmp('move-next').show();
			Ext.getCmp('move-finish').hide();
	   }
    };
    
	var cardForm = new Ext.form.FormPanel({
       	id: 'regFormPanel',
		frame: true,
		region: 'center',
        url: 'user.register.php',
        method: 'POST',
		bodyStyle: 'padding:5px',
        layout:'card',
        activeItem: 0,
        defaults: {
            border: false
        },
        items: [{
			id: 'card-0',
			items: [{
				html: '<p>Step 1 of 5</p>'
			},{
				xtype: 'panel',
				layout: 'fit',
				autoScroll: true,
				contentEl: 'x-registration-welcome'
			}]
		},{
			id: 'card-1',
			items: [{
				html: '<p>Step 2 of 5</p>'
			},{
				xtype: 'fieldset',
				title: 'User',
				autoHeight: true,
				defaultType: 'textfield',
				collapsible: true,
				items: [{
					fieldLabel: 'username',
					name: 'username',
					allowBlank: false
				}, {
					fieldLabel: 'password',
					name: 'password',
					inputType: 'password',
					allowBlank: false
				}, {
					fieldLabel: 'confirm',
					name: 'confirm',
					inputType: 'password',
					allowBlank: false
				}, new Ext.form.ComboBox({
					fieldLabel: 'language',
					hiddenName: 'language',
					store: new Ext.data.SimpleStore({
						fields: ['lang', 'language'],
						data: Ext.exampledata.language // from language.js
					}),
					valueField: 'lang',
					displayField: 'language',
					typeAhead: true,
					mode: 'local',
					triggerAction: 'all',
					emptyText: 'select a ...',
					selectOnFocus: true
				}), {
					fieldLabel: 'e-mail',
					name: 'email',
					anchor: '100%',
					allowBlank: false
				}]
			}]
		},{
			id: 'card-2',
			items: [{
				html: '<p>Step 3 of 5</p>'
			}, {
				xtype: 'fieldset',
				title: 'personals',
				collapsible: true,
				autoHeight: true,
				defaultType: 'textfield',
				items: [{
					fieldLabel: 'gender',
					name: 'gender',
					anchor: '100%',
					allowBlank: true
				}, {
					fieldLabel: 'firstname',
					name: 'firstname',
					anchor: '100%',
					allowBlank: false
				}, {
					fieldLabel: 'familyname',
					name: 'lastname',
					anchor: '100%',
					allowBlank: false
				}, new Ext.form.ComboBox({
					fieldLabel: 'country',
					hiddenName: 'state',
					store: new Ext.data.SimpleStore({
						fields: ['abbr', 'state'],
						data: Ext.exampledata.states // from states.js
					}),
					valueField: 'abbr',
					displayField: 'state',
					typeAhead: true,
					mode: 'local',
					triggerAction: 'all',
					emptyText: 'Select a...',
					selectOnFocus: true
				}), new Ext.form.DateField({
					fieldLabel: 'birthday',
					name: 'birthday',
					allowBlank: false
				})]
			}]
		},{
			id: 'card-3',
			items: [{
				html: '<p>Step 4 of 5</p>'
			},{
				xtype: 'panel',
				layout: 'fit',
				contentEl: 'x-registration-aggreement',
				autoScroll: true,
				height: 200,
				frame: true
			},{
				xtype: 'radio',
				name: 'disc_accept',
				value: "true",
				boxLabel: 'I accept the license aggrement',
				allowBlank: false
			},{
				xtype: 'radio',
				name: 'disc_accept',
				value: "false",
				checked: true,
				boxLabel: 'I do not accept the license aggrement'
			}]
		},{
            id: 'card-4',			
			items: [{
				html: '<p>Step 5 of 5</p>'
			},{          
				contentEl: 'x-registration-summary'
        	}]
		}]
    });
    
	var submitRegistration = function() {
       if ( cardForm.form.isValid() ) {
          Ext.MessageBox.alert('Status', 'Passed');
          cardForm.form.submit({
            waitMsg:'Registring new user...',
            reset: false,
            failure: function(result, action) {
              Ext.MessageBox.alert('Error', action.result.message);
            },
            success: function(result, action) {
				Ext.Msg.show({
				   title:'registration complete',
				   msg: result.responseText,
				   buttons: Ext.Msg.OK,
				   icon: Ext.MessageBox.INFO
				});
			}
          });
        } else {
          Ext.MessageBox.alert('Status', 'Not valid');
        }    
    };
	    
    var registerWin = new Ext.Window({
      id: 'regWindow',
      title: 'register a new user',
      width: 600,
	  height: 380,
      layout: 'border',
      plain: true,
      bodyStyle:'padding:5px;',
      buttonAlign:'right',
      closable: true,
      modal: true,
      items: [{		
		  	region: 'west',
			width: 150,
			html: '<img src="gfx/splash/register.jpg" />'
		  }, cardForm
	  ],
	  buttons:[{
	  	text: 'abort',
		handler: function(){
			registerWin.hide();
		}
	  },{
	  	text: 'previous',
		disabled: true,
        id: 'move-prev',
        handler: navHandler.createDelegate(this, [-1])
	  },{
	  	text: 'next',
        id: 'move-next',
        handler: navHandler.createDelegate(this, [1])
 	  },{
	  	text: 'finish',
		id: 'move-finish',
		hidden: true,
		handler: function() {
			submitRegistration();
		}
	  }]
    });
		
    return {
        init: function() {
			registerWin.show();
	        var nav = new Ext.KeyNav('regWindow', {
				"enter": function() {},
				"esc": registerWin.hide
	        });
        }
    };
}();

