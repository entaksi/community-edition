/*
 * Copyright (C) 2005-2010 Alfresco Software Limited.
 *
 * This file is part of the Alfresco Web Quick Start module.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.alfresco.module.org_alfresco_module_wcmquickstart.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.alfresco.model.ContentModel;
import org.alfresco.module.org_alfresco_module_wcmquickstart.publish.PublishService;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.namespace.RegexQNamePattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class describes an action that is designed to be called from the review-publish process. It causes the
 * content of the workflow package to be queued for publishing.
 * 
 * @author Brian
 *
 */
public class WebPublishAction extends ActionExecuterAbstractBase
{
    private final static Log log = LogFactory.getLog(WebPublishAction.class);
    
    private final static QName TYPE_BPM_PACKAGE = QName.createQName(NamespaceService.BPM_MODEL_1_0_URI, "package");
    private final static QName ASSOC_BPM_PACKAGE_CONTAINS = QName.createQName(NamespaceService.BPM_MODEL_1_0_URI, "packageContains");
    private final static QName ASPECT_BPM_WORKFLOW_PACKAGE = QName.createQName(NamespaceService.BPM_MODEL_1_0_URI, "workflowPackage");

    private NodeService nodeService;
    private PublishService publishService;
    
    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }

    public void setPublishService(PublishService publishService)
    {
        this.publishService = publishService;
    }

    /**
     * If the actionedUponNodeRef is a workflow package then this places the content of that
     * package on the queue to be published. If not then the actioned node itself will
     * be enqueued.
     */
    @Override
    protected void executeImpl(Action action, NodeRef actionedUponNodeRef)
    {
        if (log.isDebugEnabled())
        {
            log.debug("executeImpl on node " + actionedUponNodeRef);
        }
        Set<NodeRef> nodesToPublish = new HashSet<NodeRef>();
        if (nodeService.hasAspect(actionedUponNodeRef, ASPECT_BPM_WORKFLOW_PACKAGE))
        {
            if (log.isDebugEnabled())
            {
                log.debug("Supplied node is a workflow package: " + actionedUponNodeRef);
            }
            List<ChildAssociationRef> childAssocs = new ArrayList<ChildAssociationRef>();
            childAssocs.addAll(nodeService.getChildAssocs(actionedUponNodeRef, ContentModel.ASSOC_CONTAINS, RegexQNamePattern.MATCH_ALL));
            if (TYPE_BPM_PACKAGE.equals(nodeService.getType(actionedUponNodeRef)))
            {
                childAssocs.addAll(nodeService.getChildAssocs(actionedUponNodeRef, ASSOC_BPM_PACKAGE_CONTAINS, RegexQNamePattern.MATCH_ALL));
            } 
            
            for (ChildAssociationRef childAssoc : childAssocs)
            {
                nodesToPublish.add(childAssoc.getChildRef());
            }
        }
        else
        {
            nodesToPublish.add(actionedUponNodeRef);
        }
        if (log.isDebugEnabled())
        {
            log.debug("Queuing up nodes for publishing: " + nodesToPublish);
        }
        publishService.enqueuePublishedNodes(nodesToPublish);
    }

    @Override
    protected void addParameterDefinitions(List<ParameterDefinition> paramList)
    {
    }
}
