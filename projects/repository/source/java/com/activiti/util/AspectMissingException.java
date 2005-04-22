/**
 * Created on Apr 22, 2005
 */
package com.activiti.util;

import java.text.MessageFormat;

import com.activiti.repo.dictionary.ClassRef;
import com.activiti.repo.ref.NodeRef;

/**
 * Used to indicate that an aspect is missing from a node.
 * 
 * @author Roy Wetherall
 */
public class AspectMissingException extends RuntimeException
{
    /**
     * Serial version UID 
     */
    private static final long serialVersionUID = 3257852099244210228L;

    /**
     * Error message
     */
    private static final String ERROR_MESSAGE = "The {0} aspect is missing from this node (id: {1}).  " +
            "It is required for this operation.";
    
    /**
     * Constructor
     */
    public AspectMissingException(ClassRef missingAspect, NodeRef nodeRef)
    {
        super(MessageFormat.format(ERROR_MESSAGE, new Object[]{missingAspect.getQName().getLocalName(), nodeRef.getId()}));
    }
}
