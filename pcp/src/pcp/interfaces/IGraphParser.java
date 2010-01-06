package pcp.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import pcp.entities.PartitionedGraphBuilder;

public interface IGraphParser {
	
	PartitionedGraphBuilder parse(BufferedReader reader) 
		throws ParseException, IOException;
	
}