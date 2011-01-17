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
package org.alfresco.module.org_alfresco_module_wcmquickstart.model;

import org.alfresco.service.namespace.QName;

/**
 * Interface containing Web Site model constants
 * 
 * @author Roy Wetherall
 */
public interface WebSiteModel
{
    // Model details
    public static final String NAMESPACE = "http://www.alfresco.org/model/website/1.0";
    public static final String PREFIX = "ws";

    // ws:websiteContainer type
    public static final QName TYPE_WEBSITE_CONTAINER = QName.createQName(NAMESPACE, "websiteContainer");

    // ws:website type
    public static final QName TYPE_WEB_SITE = QName.createQName(NAMESPACE, "website");
    public static final QName PROP_HOST_NAME = QName.createQName(NAMESPACE, "hostName");
    public static final QName PROP_HOST_PORT = QName.createQName(NAMESPACE, "hostPort");
    public static final QName PROP_WEB_APP_CONTEXT = QName.createQName(NAMESPACE, "webAppContext");
    public static final QName PROP_FEEDBACK_CONFIG = QName.createQName(NAMESPACE, "feedbackConfig");
    public static final QName PROP_SITE_LOCALES = QName.createQName(NAMESPACE, "siteLocales");
    public static final QName PROP_WEBSITE_CONFIG = QName.createQName(NAMESPACE, "siteConfig");
    public static final QName ASSOC_PUBLISH_TARGET = QName.createQName(NAMESPACE, "publishTarget");
    public static final QName ASSOC_PUBLISHING_QUEUE = QName.createQName(NAMESPACE, "publishingQueue");

    // ws:webroot type
    public static final QName TYPE_WEB_ROOT = QName.createQName(NAMESPACE, "webroot");

    // ws:publishqueue type
    public static final QName TYPE_PUBLISH_QUEUE_ENTRY = QName.createQName(NAMESPACE, "publishqueueentry");
    public static final QName PROP_QUEUED_NODE = QName.createQName(NAMESPACE, "queuedNode");

    // ws:section type
    public static final QName TYPE_SECTION = QName.createQName(NAMESPACE, "section");
    public static final QName PROP_SECTION_CONFIG = QName.createQName(NAMESPACE, "sectionConfig");
    public static final QName PROP_INHERIT_RENDITION_CONFIG = QName.createQName(NAMESPACE, "inheritRenditionConfig");
    public static final QName PROP_RENDITION_CONFIG = QName.createQName(NAMESPACE, "renditionConfig");
    public static final QName PROP_EXCLUDE_FROM_NAV = QName.createQName(NAMESPACE, "excludeFromNavigation");
    public static final QName PROP_TOP_TAGS = QName.createQName(NAMESPACE, "topTags");
    public static final QName PROP_TOP_TAG_COUNTS = QName.createQName(NAMESPACE, "topTagCounts");

    // ws:sectionWebassetCollections type
    public static final QName TYPE_WEBASSET_COLLECTION_FOLDER = QName
            .createQName(NAMESPACE, "webassetCollectionFolder");

    // ws:webassetCollection type
    public static final QName TYPE_WEBASSET_COLLECTION = QName.createQName(NAMESPACE, "webassetCollection");
    public static final QName PROP_QUERY = QName.createQName(NAMESPACE, "query");
    public static final QName PROP_QUERY_LANGUAGE = QName.createQName(NAMESPACE, "queryLanguage");
    public static final QName PROP_QUERY_RESULTS_MAX_SIZE = QName.createQName(NAMESPACE, "queryResultsMaxSize");
    public static final QName PROP_IS_DYNAMIC = QName.createQName(NAMESPACE, "isDynamic");
    public static final QName PROP_MINS_TO_QUERY_REFRESH = QName.createQName(NAMESPACE, "minsToQueryRefresh");
    public static final QName PROP_REFRESH_AT = QName.createQName(NAMESPACE, "refreshAt");
    public static final QName ASSOC_WEBASSETS = QName.createQName(NAMESPACE, "webassets");

    // ws:indexPage type
    public static final QName TYPE_INDEX_PAGE = QName.createQName(NAMESPACE, "indexPage");

    // ws:webasset aspect
    public static final QName ASPECT_WEBASSET = QName.createQName(NAMESPACE, "webasset");
    public static final QName PROP_PARENT_SECTIONS = QName.createQName(NAMESPACE, "parentSections");

    // ws:hasancestors aspect
    public static final QName ASPECT_HAS_ANCESTORS = QName.createQName(NAMESPACE, "hasancestors");
    public static final QName PROP_ANCESTOR_SECTIONS = QName.createQName(NAMESPACE, "ancestorSections");

    // ws:imageFolder type
    public static final QName TYPE_IMAGE = QName.createQName(NAMESPACE, "image");
    public static final QName PROP_DERIVED_COMMENT_COUNT = QName.createQName(NAMESPACE, "derivedCommentCount");
    public static final QName PROP_DERIVED_AVERAGE_RATING = QName.createQName(NAMESPACE, "derivedAverageRating");
    public static final QName PROP_AVAILABLE_FROM_DATE = QName.createQName(NAMESPACE, "availableFromDate");
    public static final QName PROP_AVAILABLE_TO_DATE = QName.createQName(NAMESPACE, "availableToDate");
    public static final QName PROP_PUBLISHED_TIME = QName.createQName(NAMESPACE, "publishedTime");
    // public static final QName PROP_PUBLISHED = QName.createQName(NAMESPACE,
    // "available");
    public static final QName PROP_AVAILABLE = QName.createQName(NAMESPACE, "available");
    public static final QName PROP_TAGS = QName.createQName(NAMESPACE, "tags");

    // ws:visitorFeedback type
    public static final QName TYPE_VISITOR_FEEDBACK = QName.createQName(NAMESPACE, "visitorFeedback");
    public static final QName ASSOC_RELEVANT_ASSET = QName.createQName(NAMESPACE, "relevantAssetAssoc");
    public static final QName PROP_RELEVANT_ASSET = QName.createQName(NAMESPACE, "relevantAssetRef");
    public static final QName PROP_RATING_PROCESSED = QName.createQName(NAMESPACE, "ratingProcessed");
    public static final QName PROP_COMMENT_FLAGGED = QName.createQName(NAMESPACE, "commentFlagged");
    public static final QName PROP_COMMENT = QName.createQName(NAMESPACE, "feedbackComment");
    public static final QName PROP_RATING = QName.createQName(NAMESPACE, "rating");
    public static final QName PROP_FEEDBACK_TYPE = QName.createQName(NAMESPACE, "feedbackType");
    public static final QName PROP_VISITOR_NAME = QName.createQName(NAMESPACE, "visitorName");
    public static final QName PROP_VISITOR_EMAIL = QName.createQName(NAMESPACE, "visitorEmail");

    // ws:visitorFeedbackSummary type
    public static final QName TYPE_VISITOR_FEEDBACK_SUMMARY = QName.createQName(NAMESPACE, "visitorFeedbackSummary");
    public static final QName ASSOC_SUMMARISED_ASSET = QName.createQName(NAMESPACE, "summarisedAssetAssoc");
    public static final QName PROP_AVERAGE_RATING = QName.createQName(NAMESPACE, "averageRating");
    public static final QName PROP_COMMENT_COUNT = QName.createQName(NAMESPACE, "commentCount");
    public static final QName PROP_PROCESSED_RATINGS = QName.createQName(NAMESPACE, "processedRatings");
    public static final QName PROP_SUMMARISED_ASSET = QName.createQName(NAMESPACE, "summarisedAssetRef");

    // ws:article type
    public static final QName TYPE_ARTICLE = QName.createQName(NAMESPACE, "article");
    public static final QName ASSOC_PRIMARY_IMAGE = QName.createQName(NAMESPACE, "primaryImage");
    public static final QName ASSOC_SECONDARY_IMAGE = QName.createQName(NAMESPACE, "secondaryImage");

    // Process details
    public static final String PROCESS_READ_CONTACT = "jbpm$wswf:readContactProcess";
}
