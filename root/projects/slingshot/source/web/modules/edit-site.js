/**
 * Copyright (C) 2005-2008 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * and Open Source Software ("FLOSS") applications as described in Alfresco's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * http://www.alfresco.com/legal/licensing
 */

/**
 * EditSite module
 *
 * A dialog for creating sites
 *
 * @namespace Alfresco.module
 * @class Alfresco.module.EditSite
 */
(function()
{
   
   var Dom = YAHOO.util.Dom;
   
   /**
    * EditSite constructor.
    *
    * @param htmlId {string} A unique id for this component
    * @return {Alfresco.EditSite} The new DocumentList instance
    * @constructor
    */
   Alfresco.module.EditSite = function(containerId)
   {
      this.name = "Alfresco.module.EditSite";
      this.id = containerId;

      var instance = Alfresco.util.ComponentManager.get(this.id);
      if (instance !== null)
      {
         throw new Error("An instance of Alfresco.module.EditSite already exists.");
      }

      // Set protype variables
      this.editPanelActive = false;

      // Register this component
      Alfresco.util.ComponentManager.register(this);

      // Load YUI Components
      Alfresco.util.YUILoaderHelper.require(["button", "container", "connection", "selector", "json", "event"], this.onComponentsLoaded, this);

      return this;
   };

   Alfresco.module.EditSite.prototype =
   {

      /**
       * True if the delete prompt is shown
       *
       * @property: visible
       * @type: boolean
       */
      editPanelActive: false,

      /**
       * Object container for storing YUI widget instances.
       * 
       * @property widgets
       * @type object
       */
       widgets: {},

      /**
       * Set messages for this module.
       *
       * @method setMessages
       * @param obj {object} Object literal specifying a set of messages
       * @return {Alfresco.module.EditSite} returns 'this' for method chaining
       */
      setMessages: function CS_setMessages(obj)
      {
         Alfresco.util.addMessages(obj, this.name);
         return this;
      },

      /**
       * Fired by YUILoaderHelper when required component script files have
       * been loaded into the browser.
       *
       * @method onComponentsLoaded
       */
      onComponentsLoaded: function CS_onComponentsLoaded()
      {
         if (this.id === null)
         {
            return;
         }
      },


      /**
       * The default config for the gui state for edit site.
       *
       * @property defaultShowConfig
       * @type object
       */
      defaultShowConfig:
      {
      },

      /**
       * The merged result of the defaultShowConfig and the config passed in
       * to the show method.
       *
       * @property defaultShowConfig
       * @type object
       */
      showConfig: {},

      /**
       * Shows the CreteSite dialog to the user.
       *
       * @method show
       */
      show: function CS_show(config)
      {
         if(!this.editPanelActive)
         {
            // Set editPanelActive to true so we don't open multiple dialogs
            this.editPanelActive = true;

            // Merge the supplied config with default config and check mandatory properties
            this.showConfig = YAHOO.lang.merge(this.defaultShowConfig, config);
            if (this.showConfig.shortName === undefined)
            {
               this.editPanelActive = false;
               throw new Error("A shortName must be provided");
            }

            if (this.widgets.panel)
            {
               this.widgets.panel.destroy();
               this.widgets = {};
            }

            /**
             * Load the gui and site info from the server and let the templateLoaded() method
             * handle the rest.
             */
            Alfresco.util.Ajax.request(
            {
               url: Alfresco.constants.URL_SERVICECONTEXT + "modules/edit-site",
               dataObj:
               {
                  htmlid: this.id,
                  shortName: this.showConfig.shortName
               },
               successCallback:
               {
                  fn: this.onTemplateLoaded,
                  scope: this
               },
               execScripts: true,
               failureMessage: "Could not load edit site template"
            });
         }
      },

      /**
       * Called when the EditSite html template has been returned from the server.
       * Creates the YUI gui objects such as buttons and a panel and shows it.
       *
       * @method onTemplateLoaded
       * @param response {object} a Alfresco.util.Ajax.request response object 
       */
      onTemplateLoaded: function CS_onTemplateLoaded(response)
      {
         // Inject the template from the XHR request into a new DIV element
         var containerDiv = document.createElement("div");
         containerDiv.innerHTML = response.serverResponse.responseText;

         // The panel is created from the HTML returned in the XHR request, not the container
         var panelDiv = YAHOO.util.Dom.getFirstChild(containerDiv);

         this.widgets.panel = new YAHOO.widget.Panel(panelDiv,
         {
            modal: true,
            draggable: false,
            fixedcenter: true,
            close: false,
            visible: false
         });

         // Add it to the Dom
         this.widgets.panel.render(document.body);

         // Create the cancel button
         this.widgets.cancelButton = Alfresco.util.createYUIButton(this, "cancel-button", this.onCancelButtonClick);

         // Create the ok button, the forms runtime will handle when its clicked
         this.widgets.okButton = Alfresco.util.createYUIButton(this, "ok-button", null,
         {
            type: "submit"
         });

         // Configure the forms runtime
         var editSiteForm = new Alfresco.forms.Form(this.id + "-form");

         // Title is mandatory
         editSiteForm.addValidation(this.id + "-title", Alfresco.forms.validation.mandatory, null, "keyup");
         // ...and has a maximum length
         editSiteForm.addValidation(this.id + "-title", Alfresco.forms.validation.length,
         {
            max: 256,
            crop: true
         }, "keyup");

         // Description kept to a reasonable length
         editSiteForm.addValidation(this.id + "-description", Alfresco.forms.validation.length,
         {
            max: 512,
            crop: true
         }, "keyup");

         // The ok button is the submit button, and it should be enabled when the form is ready
         editSiteForm.setShowSubmitStateDynamically(true, false);
         editSiteForm.setSubmitElements(this.widgets.okButton);
         editSiteForm.doBeforeFormSubmit =
         {
            fn: function()
            {
               var formEl = YAHOO.util.Dom.get(this.id + "-form");
               formEl.attributes.action.nodeValue = Alfresco.constants.PROXY_URI + "api/sites/" + this.showConfig.shortName; 

               this.widgets.okButton.set("disabled", true);
               this.widgets.cancelButton.set("disabled", true);
               this.widgets.panel.hide();
               this.widgets.feedbackMessage = Alfresco.util.PopupManager.displayMessage(
               {
                  text: Alfresco.util.message("message.saving", this.name),
                  spanClass: "wait",
                  displayTime: 0
               });
            },
            obj: null,
            scope: this
         }

         // Submit as an ajax submit (not leave the page), in json format
         editSiteForm.setAJAXSubmit(true,
         {
            successCallback:
            {
               fn: this.onEditSiteSuccess,
               scope: this               
            },
            failureCallback:
            {
               fn: this.onEditSiteFailure,
               scope: this
            }
         });
         editSiteForm.setSubmitAsJSON(true);
         editSiteForm.setAjaxSubmitMethod("PUT");
         // We're in a popup, so need the tabbing fix
         editSiteForm.applyTabFix();
         editSiteForm.init();

         this.widgets.isPublicCheckbox = Dom.get(this.id + "-isPublic-checkbox");
         YAHOO.util.Event.addListener(this.widgets.isPublicCheckbox, "change", this.onIsPublicChange, this, true);
         this.widgets.isPublic = Dom.get(this.id + "-isPublic");

         // Show the panel
         this._showPanel();
      },

      /**
       * Called when user clicks on the isPublic checkbox.
       *
       * @method onCancelButtonClick
       * @param type
       * @param args
       */
      onIsPublicChange: function CS_onIsPublicChange(type, args)
      {
         this.widgets.isPublic.value = this.widgets.isPublicCheckbox.checked ? "true" : "false";
      },

      /**
       * Called when user clicks on the cancel button.
       * Closes the EditSite panel.
       *
       * @method onCancelButtonClick
       * @param type
       * @param args
       */
      onCancelButtonClick: function CS_onCancelButtonClick(type, args)
      {
         this.widgets.panel.hide();
         this.editPanelActive = false;
      },

      /**
       * Called when a site has been succesfully created on the server.
       * Redirects the user to the new site.
       *
       * @method onEditSiteSuccess
       * @param response
       */
      onEditSiteSuccess: function CS_onEditSiteSuccess(response)
      {
         if (response.json !== undefined && response.json.shortName)
         {
            // The site has been successfully created, redirect the user to it.
            document.location.href = Alfresco.constants.URL_CONTEXT + "page/site/" + response.json.shortName + "/dashboard";
         }
         else
         {
            this._adjustGUIAfterFailure(response);
         }
      },

      /**
       * Called when a site failed to be created.
       *
       * @method onEditSiteFailure
       * @param response
       */
      onEditSiteFailure: function CS_onEditSiteFailure(response)
      {
         this._adjustGUIAfterFailure(response);
      },

      /**
       * Helper method that restores the gui and displays an error message.
       *
       * @method _adjustGUIAfterFailure
       * @param response
       */
      _adjustGUIAfterFailure: function CS__adjustGUIAfterFailure(response)
      {
         this.widgets.feedbackMessage.destroy();
         this.widgets.okButton.set("disabled", false);
         this.widgets.cancelButton.set("disabled", false);
         this.widgets.panel.show();
         var text = Alfresco.util.message("message.failure", this.name);
         if (response.json.message)
         {
            var tmp = Alfresco.util.message(response.json.message, this.name);
            text = tmp ? tmp : text;
         }
         Alfresco.util.PopupManager.displayPrompt(
         {
            text: text
         });
      },

      /**
       * Prepares the gui and shows the panel.
       *
       * @method _showPanel
       * @private
       */
      _showPanel: function CS__showPanel()
      {
         // Show the upload panel
         this.widgets.panel.show();

         // Firefox insertion caret fix
         Alfresco.util.caretFix(this.id + "-form");

         // Register the ESC key to close the dialog
         var escapeListener = new YAHOO.util.KeyListener(document,
         {
            keys: YAHOO.util.KeyListener.KEY.ESCAPE
         },
         {
            fn: function(id, keyEvent)
            {
               this.onCancelButtonClick();
            },
            scope: this,
            correctScope: true
         });
         escapeListener.enable();

         // Set the focus on the first field
         YAHOO.util.Dom.get(this.id + "-title").focus();
      }

   };
})();

Alfresco.module.getEditSiteInstance = function()
{
   var instanceId = "alfresco-editSite-instance";
   return Alfresco.util.ComponentManager.get(instanceId) || new Alfresco.module.EditSite(instanceId);
}