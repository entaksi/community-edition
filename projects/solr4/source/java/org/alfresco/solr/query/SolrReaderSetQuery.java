/*
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.solr.query;

import java.io.IOException;

import org.alfresco.repo.search.adaptor.lucene.QueryConstants;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.Bits;
import org.apache.solr.search.SolrIndexSearcher;

/**
 * @author Andy
 * @author Matt Ward
 */
public class SolrReaderSetQuery extends AbstractAuthoritySetQuery
{
    public SolrReaderSetQuery(String authorities)
    {
        super(authorities);
    }
    
    @Override
    public Weight createWeight(IndexSearcher searcher) throws IOException
    {
        if(!(searcher instanceof SolrIndexSearcher))
        {
            throw new IllegalStateException("Must have a SolrIndexSearcher");
        }
        return new SolrReaderSetQueryWeight((SolrIndexSearcher)searcher, this, authorities);
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(QueryConstants.FIELD_READERSET).append(':');
        stringBuilder.append(authorities);
        return stringBuilder.toString();
    }

    class SolrReaderSetQueryWeight extends AbstractAuthorityQueryWeight
    {
        public SolrReaderSetQueryWeight(SolrIndexSearcher searcher, Query query, String readers) throws IOException
        {
            super(searcher, query, QueryConstants.FIELD_READERSET, readers);
        }

        @Override
        public Scorer scorer(AtomicReaderContext context, Bits acceptDocs) throws IOException
        {
            AtomicReader reader = context.reader();
            return SolrReaderSetScorer.createReaderSetScorer(this, context, searcher, authorities, reader);
        }
    }
}