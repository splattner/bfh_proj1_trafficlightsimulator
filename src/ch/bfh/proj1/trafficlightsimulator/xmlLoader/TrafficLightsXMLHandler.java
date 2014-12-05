package ch.bfh.proj1.trafficlightsimulator.xmlLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import ch.bfh.proj1.trafficlightsimulator.Junction;
import ch.bfh.proj1.trafficlightsimulator.Lane;
import ch.bfh.proj1.trafficlightsimulator.Route;
import ch.bfh.proj1.trafficlightsimulator.Street;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class TrafficLightsXMLHandler {
	
	private Collection<Junction> junctions;
	private Collection<Street> streets;
	private Collection<Lane> lanes;
	private Collection<Route> routes;
	
	public Collection<Junction> getJunctions () {return junctions;}
	public Collection<Street> getStreets () {return streets;}
	public Collection<Lane> getLanes () {return lanes;}
	public Collection<Route> getRoutes () {return routes;}
	
	public TrafficLightsXMLHandler (String xmlFilePath)
    {
        try {
        	
            /* create a JAXBContext capable of handling classes generated into
             * the xmlLoader package
             */
            JAXBContext jc = JAXBContext.newInstance("ch.bfh.proj1.trafficlightsimulator.xmlLoader");
            
            // create an Unmarshaller
            Unmarshaller u = jc.createUnmarshaller();
            
            /*
             * unmarshal a configuration instance document into a tree of Java content
             * objects composed of classes from the xmlLoader package.
             */
            
            JAXBElement<?> configElement = (JAXBElement<?>)u.unmarshal( new FileInputStream(xmlFilePath));
            ConfigType ct = (ConfigType)configElement.getValue();
            
            junctions = new LinkedList<Junction>();
            
            for (JunctionType jt : ct.getJunctions().getJunction())
            {
            	Junction j = new Junction(jt.getId());
            	junctions.add(j);
            }
            
            streets = new LinkedList<Street>();
            
            for (StreetType st : ct.getStreets().getStreet())
            {
				Street s = new Street(st.getId());
				
				s.setOrientaion(Enum.valueOf(Street.orientation.class, st.getOrientation()));
				
				if (st.getStartJunction() != null)
				{
					for (Junction j : junctions)
					{
						if (j.getId() == st.getStartJunction())
						{
							s.setStartJunction(j);
							break;
						}
					}
				}

				if(st.getEndJunction() != null)
				{
					for (Junction j : junctions)
					{
						if (j.getId() == st.getEndJunction())
						{
							s.setEndJunction(j);
							break;
						}
					}
				}
				
				streets.add(s);
            }
            
			lanes = new LinkedList<Lane>();

			for (LaneType lt : ct.getLanes().getLane()) 
			{
				Lane l = new Lane(lt.getId(), Enum.valueOf(Lane.laneOrientations.class, lt.direction));
				lanes.add(l);
				for (Street s : streets)
				{
					if (s.getId()==lt.getStreet())
					{
						s.addLane(l);
						break;
					}
				}
			}
			
			routes = new ArrayList<Route>();

			
			for (RouteType rt : ct.getRoutes().getRoute())
			{
				Route r = new Route(rt.getId());
				
				for(int j = 0; j<rt.getLane().size();j++)
				{
					for (Lane l : lanes)
					{
						if (l.getId() == rt.getLane().get(j))
						{
							r.addLane(l);
							r.setDistribution(100/ct.getRoutes().getRoute().size());
							break;
						}
					}
				}
				
				routes.add(r);
			}
			
			for (JunctionType jt : ct.getJunctions().getJunction())
			{
				for(Junction j : junctions)
				{
					if (jt.getId() == j.getId())
					{
						if(jt.getBottomStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == jt.getBottomStreet())
										j.setBottomStreet(s);
							}
						}

						if(jt.getLeftStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == jt.getLeftStreet())
										j.setLeftStreet(s);
							}
						}
						
						if(jt.getRightStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == jt.getRightStreet())
										j.setRightStreet(s);
							}
						}
						
						if(jt.getTopStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == jt.getTopStreet())
										j.setTopStreet(s);
							}
						}
						
						break;
					}
				}
			}
            
        } catch( JAXBException je ) {
            je.printStackTrace();
        } catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }
}
