package xmlLoader;

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
	
	private static Collection<Junction> junctions;
	private static Collection<Street> streets;
	private static Collection<Lane> lanes;
	private static Collection<Route> routes;
	
	public Collection<Junction> getJunctions () {return junctions;}
	public Collection<Street> getStreets () {return streets;}
	public Collection<Lane> getLanes () {return lanes;}
	public Collection<Route> getRoutes () {return routes;}
	
//    public static void main( String[] args )
	public TrafficLightsXMLHandler ()
    {
        try {
        	
            /* create a JAXBContext capable of handling classes generated into
             * the xmlLoader package
             */
            JAXBContext jc = JAXBContext.newInstance("xmlLoader");
            
            // create an Unmarshaller
            Unmarshaller u = jc.createUnmarshaller();
            
            /*
             * unmarshal a configuration instance document into a tree of Java content
             * objects composed of classes from the xmlLoader package.
             */
            
            JAXBElement<?> poElement = (JAXBElement<?>)u.unmarshal( new FileInputStream("TrafficLightsConfig1.xml"));
            ConfigType ct = (ConfigType)poElement.getValue();
            
            junctions = new LinkedList<Junction>();
            
            for (int i = 0; i<ct.getJunctions().getJunction().size(); i++)
            {
            	Junction j = new Junction(ct.getJunctions().getJunction().get(i).getId());
            	junctions.add(j);
            }
            
            streets = new LinkedList<Street>();
            
            for (int i = 0; i<ct.getStreets().getStreet().size(); i++)
            {
				Street s = new Street(ct.getStreets().getStreet().get(i).getId());
				
				s.setOrientaion(Enum.valueOf(Street.orientation.class, ct.getStreets().getStreet().get(i).getOrientation()));
				
				if (ct.getStreets().getStreet().get(i).getStartJunction() != null)
				{
					for (Junction j : junctions)
					{
						if (j.getId() == ct.getStreets().getStreet().get(i).getStartJunction())
						{
							s.setStartJunction(j);
							break;
						}
					}
				}

				if(ct.getStreets().getStreet().get(i).getEndJunction() != null)
				{
					for (Junction j : junctions)
					{
						if (j.getId() == ct.getStreets().getStreet().get(i).getEndJunction())
						{
							s.setEndJunction(j);
							break;
						}
					}
				}
				
				streets.add(s);
            }
            
			lanes = new LinkedList<Lane>();
			
			for (int i = 0; i < ct.getLanes().getLane().size(); i++) 
			{
				Lane l = new Lane(ct.getLanes().getLane().get(i).getId(),
						Enum.valueOf(Lane.laneOrientations.class, ct.getLanes().getLane().get(i).direction));
				lanes.add(l);
				for (Street s : streets)
				{
					if (s.getId()==ct.getLanes().getLane().get(i).getStreet())
					{
						s.addLane(l);
						break;
					}
				}
			}
			
			routes = new ArrayList<Route>();
			
			for (int i = 0; i < ct.getRoutes().getRoute().size(); i++)
			{
				Route r = new Route(ct.getRoutes().getRoute().get(i).getId());
				
				for(int j = 0; j<ct.getRoutes().getRoute().get(i).getLane().size();j++)
				{
					for (Lane l : lanes)
					{
						if (l.getId() == ct.getRoutes().getRoute().get(i).getLane().get(j))
						{
							r.addLane(l);
							break;
						}
					}
				}
				
				routes.add(r);
			}
			
			for (int i = 0; i<ct.getJunctions().getJunction().size(); i++)
			{
				for(Junction j : junctions)
				{
					if (ct.getJunctions().getJunction().get(i).getId() == j.getId())
					{
						if(ct.getJunctions().getJunction().get(i).getBottomStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == ct.getJunctions().getJunction().get(i).getBottomStreet())
										j.setBottomStreet(s);
							}
						}

						if(ct.getJunctions().getJunction().get(i).getLeftStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == ct.getJunctions().getJunction().get(i).getLeftStreet())
										j.setLeftStreet(s);
							}
						}
						
						if(ct.getJunctions().getJunction().get(i).getRightStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == ct.getJunctions().getJunction().get(i).getRightStreet())
										j.setRightStreet(s);
							}
						}
						
						if(ct.getJunctions().getJunction().get(i).getTopStreet()!=null)
						{
							for(Street s : streets)
							{
								if (s.getId() == ct.getJunctions().getJunction().get(i).getTopStreet())
										j.setTopStreet(s);
							}
						}
						
						break;
					}
				}
			}

/*
 * 7) set origin of first street
 */
            
        } catch( JAXBException je ) {
            je.printStackTrace();
        } catch( IOException ioe ) {
            ioe.printStackTrace();
        }
    }
}
