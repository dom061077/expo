		Ext.onReady(function () {
			
			var testWizard = new Ext.ux.PowerWizard({
		 		id: "card-emp-wizard",
		 		renderTo:'formaulario_extjs',
				title: "",
				activeItem: 0,
				
				cancelHandler: function () {
					// commands to perform upon use cancellation
					testWizard.reset(true);
					wizardWindow.hide();
				},
				submitHandler: function () {
					Ext.Msg.alert("Wizard Output", Ext.encode(this.getValues()));
				},
				items: [{
					xtype: "panel",
					id: "card-0",
					html: '<div style="font-size: 9pt;">'
						+ "Welcome to the Sample PowerWizard Application."
						+ "</div>"
				},{
					id: "card-1",
					sequenceControl: [{
						key: "opt01",
						values: {
							"Yes": 3
						}
					}],
					items: {
						id: "card-1-fieldset-1",
						title: "Are you available to work without restriction?",
						items: [{
							boxLabel: "Yes",
							inputValue: "Yes",
							name: "opt01"
						},{
			            	boxLabel: "No",
			            	inputValue: "No",
			            	name: "opt01"
						}]
					}
				},{				
					id: "card-2",
					items: {
						id: "card-2-fieldset-1",
						title: "Are you available to work from home?",
						items: [{
							boxLabel: "Yes",
							inputValue: "Yes",
							name: "opt02"
						},{
		                	boxLabel: "No",
		                	inputValue: "No",
		                	name: "opt02"
						}]
					}
				},{
					xtype: "panel",
					id: "card-3",
					html: '<div style="font-size: 9pt;">'
						+ "Your information is now complete.  Submit your form to start your employment."
						+ '</div',
					listeners: {
						"show": function () {
							Ext.Msg.alert("Alert", "Your form can now be submitted.");
						}
					}
				}]
			});
			
			/*var wizardWindow = new Ext.Window({
				closeAction: 'hide',
				layout: "fit",
				modal: true,
				plain: false,
				resizable: true,
				title: "Wizard Window",
				
				height: 400,
				minHeight: 400,
				width: 625,
				minWidth: 625,
				
				items: [
					testWizard.source
				]
			});
			
			wizardWindow.on({
				"hide": function () {
					testWizard.reset(true);
				}
			});
			
			wizardWindow.show();*/
			

			
		});