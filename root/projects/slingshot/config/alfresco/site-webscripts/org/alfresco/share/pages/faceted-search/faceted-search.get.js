<import resource="classpath:/alfresco/site-webscripts/org/alfresco/share/imports/share-header.lib.js">
<import resource="classpath:/alfresco/site-webscripts/org/alfresco/share/imports/share-footer.lib.js">

// Get Search sorting configuration from share-config
var sortConfig = config.scoped["Search"]["sorting"];

// Get the initial header services and widgets...
var services = getHeaderServices(),
    widgets = getHeaderModel(msg.get("faceted-search.page.title"));

// Scope the model IDs
var rootWidgetId = "FCTSRCH_";

// Accessibility menu
//var accessMenu = {
//   id: rootWidgetId + "ACCESSIBILITY_MENU",
//   name: "alfresco/accessibility/AccessibilityMenu",
//   config: {
//      menu: [
//         {"url": "#" + rootWidgetId + "SEARCH_FORM", "key": "f", "msg": "skip.to.content.message"},
//         {"url": "#" + rootWidgetId + "FACET_MENU", "key": "q", "msg": "skip.to.content.message"},
//         {"url": "#" + rootWidgetId + "SORT_MENU", "key": "m", "msg": "access.keys.message"},
//         {"url": "#" + rootWidgetId + "SEARCH_RESULTS_LIST", "key": "r", "msg": "access.keys.message"}
//      ]
//   }
//};

// Compose the search form model
var searchForm = {
   id: rootWidgetId + "SEARCH_FORM",
   name: "alfresco/forms/SingleTextFieldForm",
   config: {
      useHash: true,
      okButtonLabel: msg.get("faceted-search.search-form.ok-button-label"),
      okButtonPublishTopic : "ALF_SET_HASH",
      okButtonPublishGlobal: false,
      okButtonIconClass: "alf-white-search-icon",
      okButtonClass: "call-to-action",
      textFieldName: "searchTerm",
      textBoxIconClass: "alf-search-icon",
      textBoxCssClasses: "long"
   }
};

// Compose the zero results configuration
var hideOnZeroResultsConfig = {
   initialValue: true,
   rules: [
      {
         topic: "ALF_SEARCH_RESULTS_COUNT",
         attribute: "count",
         isNot: [0]
      }
   ]
};

// Compose the facet menu column
var sideBarMenu = {
   id: rootWidgetId + "FACET_MENU",
   name: "alfresco/layout/LeftAndRight",
   config: {
      visibilityConfig: hideOnZeroResultsConfig,
      widgets: [
         {
            name: "alfresco/html/Label",
            align: "left",
            config: {
               label: msg.get("faceted-search.facet-menu.instruction")
            }
         }
      ]
   }
};

// Compose the individual facets
var facets = [
   {
      id: rootWidgetId + "FACET_FORMATS",
      name: "alfresco/search/FacetFilters",
      config: {
         label: msg.get("faceted-search.facet-menu.facet.formats"),
         facetQName: "{http://www.alfresco.org/model/content/1.0}content.mimetype",
         sortBy: "DESCENDING",
         maxFilters: 6,
         useHash: true
      }
   },
   {
      id: rootWidgetId + "FACET_DESCRIPTION",
      name: "alfresco/search/FacetFilters",
      config: {
         label: msg.get("faceted-search.facet-menu.facet.description"),
         facetQName: "{http://www.alfresco.org/model/content/1.0}description.__",
         sortBy: "DESCENDING",
         hitThreshold: 1,
         minFilterValueLength: 5,
         maxFilters: 6,
         useHash: true
      }
   },
   {
      id: rootWidgetId + "FACET_CREATOR",
      name: "alfresco/search/FacetFilters",
      config: {
         label: msg.get("faceted-search.facet-menu.facet.creator"),
         facetQName: "{http://www.alfresco.org/model/content/1.0}creator.__",
         sortBy: "ALPHABETICALLY",
         maxFilters: 3,
         useHash: true
      }
   },
   {
      id: rootWidgetId + "FACET_MODIFIER",
      name: "alfresco/search/FacetFilters",
      config: {
         label: msg.get("faceted-search.facet-menu.facet.modifier"),
         facetQName: "{http://www.alfresco.org/model/content/1.0}modifier.__",
         sortBy: "ALPHABETICALLY",
         maxFilters: 3,
         useHash: true
      }
   }
];

// Function to compose the sort fields from share-config
function getSortFieldsFromConfig()
{
   // Get sort fields element from the configuration
   var configSortFields = sortConfig.getChildren();

   // Initialise the sort fields array
   var sortFields = new Array(configSortFields.length);

   // Iterate over configuration sort fields
   for(var i=0; i < configSortFields.size(); i+=1)
   {
      // Extract sort properties from configuration
      var configSortField = configSortFields.get(i),
          label = String(configSortField.attributes["labelId"]),
          valueTokens = String(configSortField.value).split("|"),
          value = valueTokens[0],
          direction = "descending",
          checked = (i==0 ? true : false);

      // The value may contain 2 pieces of data - the optional 2nd is for sort direction
      if(valueTokens instanceof Array && valueTokens.length > 1 && valueTokens[1] === "true")
      {
         direction = "ascending";
      }

      // Create a new sort widget
      var sort = {
         name: "alfresco/menus/AlfCheckableMenuItem",
         config: {
            label: msg.get(label),
            value: value,
            group: "DOCUMENT_LIBRARY_SORT_FIELD",
            publishTopic: "ALF_DOCLIST_SORT_FIELD_SELECTION",
            checked: checked,
            publishPayload: {
               label: msg.get(label),
               direction: direction
            }
         }
      };

      // Add to the sortFields array
      sortFields[i] = sort;
   }
   
   return sortFields;
}

// Compose the sort menu
var sortMenu = {
   id: rootWidgetId + "SORT_MENU",
   name: "alfresco/menus/AlfMenuBarSelect",
   config: {
      visibilityConfig: hideOnZeroResultsConfig,
      selectionTopic: "ALF_DOCLIST_SORT_FIELD_SELECTION",
      widgets: [
         {
            id: "DOCLIB_SORT_FIELD_SELECT_GROUP",
            name: "alfresco/menus/AlfMenuGroup",
            config: {
               widgets: getSortFieldsFromConfig()
            }
         }
      ]
   }
};

// Compose result menu bar
var searchResultsMenuBar = {
   id: rootWidgetId + "RESULTS_MENU_BAR",
   name: "alfresco/layout/LeftAndRight",
   config: {
      widgets: [
         {
            name: "alfresco/html/Label",
            align: "left",
            config: {
               label: msg.get("faceted-search.results-menu.no-results"),
               subscriptionTopic: "ALF_SEARCH_RESULTS_COUNT"
            }
         },
         {
            name: "alfresco/menus/AlfMenuBar",
            align: "right",
            config: {
               widgets: [
                  {
                     id: rootWidgetId + "SORT_ORDER_TOGGLE",
                     name: "alfresco/menus/AlfMenuBarToggle",
                     config: {
                        visibilityConfig: hideOnZeroResultsConfig,
                        checked: true,
                        onConfig: {
                           iconClass: "alf-sort-ascending-icon",
                           publishTopic: "ALF_DOCLIST_SORT",
                           publishPayload: {
                              direction: "ascending"
                           }
                        },
                        offConfig: {
                           iconClass: "alf-sort-descending-icon",
                           publishTopic: "ALF_DOCLIST_SORT",
                           publishPayload: {
                              direction: "descending"
                           }
                        }
                     }
                  },
                  sortMenu
               ]
            }
         }
      ]
   }
};

// Build the searchDocLib model
var searchDocLib = {
   id: rootWidgetId + "SEARCH_RESULTS_LIST",
   name: "alfresco/documentlibrary/AlfSearchList",
   config: {
      useHash: true,
      useInfiniteScroll: true,
      widgets: [
         {
            name: "alfresco/documentlibrary/views/AlfSearchListView",
            config: {
               searchAdvice: [
                  "faceted-search.suggestion1",
                  "faceted-search.suggestion2",
                  "faceted-search.suggestion3",
                  "faceted-search.suggestion4"
               ]
            }
         },
         {
            name: "alfresco/documentlibrary/AlfDocumentListInfiniteScroll",
            config: {
            }
         }
      ]
   }
};

// Put all components together
var main = {
   name: "alfresco/layout/VerticalWidgets",
   config: {
      baseClass: "side-margins",
      widgets: [
//         accessMenu,
         {
            name: "alfresco/html/Spacer",
            config: {
               height: "20px",
               additionalCssClasses: "top-border-beyond-gutters"
            }
         },
         searchForm,
         {
            name: "alfresco/layout/HorizontalWidgets",
            config: {
               widgets: [
                  {
                     name: "alfresco/layout/VerticalWidgets",
                     align: "sidebar",
                     widthPx: 350,
                     config: {
                        widgets: [
                           sideBarMenu
                        ]
                     }
                  },
                  {
                     name: "alfresco/layout/VerticalWidgets",
                     config: {
                        widgets: [
                           searchResultsMenuBar
                        ]
                     }
                  }
               ]
            }
         },
         {
            name: "alfresco/layout/HorizontalWidgets",
            config: {
               widgets: [
                  {
                     name: "alfresco/layout/VerticalWidgets",
                     align: "sidebar",
                     widthPx: 350,
                     config: {
                        widgets: facets
                     }
                  },
                  {
                     name: "alfresco/layout/VerticalWidgets",
                     config: {
                        widgets: [
                           searchDocLib
                        ]
                     }
                  }
               ]
            }
         }
      ]
   }
};

// Append services with those required for search
services.push("alfresco/services/NavigationService",
              "alfresco/services/SearchService");

// Add in the search form and search doc lib...
widgets.push(main);

// Push services and widgets into the getFooterModel to return with a sticky footer wrapper
model.jsonModel = getFooterModel(services, widgets);