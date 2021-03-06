package ch.bfh.proj1.trafficlightsimulator.xmlLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import ch.bfh.proj1.trafficlightsimulator.Junction;
import ch.bfh.proj1.trafficlightsimulator.Lane;
import ch.bfh.proj1.trafficlightsimulator.Route;
import ch.bfh.proj1.trafficlightsimulator.Street;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class TrafficLightsXMLWriter {
	
	private Collection<Junction> junctions;
	private Collection<Street> streets;
	private Collection<Lane> lanes = new LinkedList<Lane>();
	private Collection<Route> routes;
	
	public Collection<Junction> getJunctions () {return junctions;}
	public Collection<Street> getStreets () {return streets;}
	public Collection<Lane> getLanes () {return lanes;}
	public Collection<Route> getRoutes () {return routes;}
	
	public TrafficLightsXMLWriter(String xmlFilePath,
			Collection<Junction> junctions, Collection<Street> streets,
			Collection<Route> routes) {
		
		this.junctions = junctions;
		this.streets = streets;
		this.routes = routes;
		
		try {

			/*
			 * create a JAXBContext capable of handling classes generated into
			 * the xmlLoader package
			 */
			JAXBContext jc = JAXBContext
					.newInstance("ch.bfh.proj1.trafficlightsimulator.xmlLoader");

			// create a Marshaller
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			/*
			 * marshal a configuration instance objects document into an XML
			 * document composed of classes from the xmlLoader package.
			 */

			ObjectFactory of = new ObjectFactory();
			
			ConfigType ct = of.createConfigType();
			
            JAXBElement<ConfigType> configElement = of.createTrafficLightConfig(ct);

            /*
             * Extract data from junction list and add related Junction information to the xml root element.
             */
            
			JunctionsType jtl = of.createJunctionsType();
			
			for (Junction j : junctions){
				JunctionType jt = new JunctionType();
				jt.setId(j.getId());
				if(j.getBottomStreet()!=null) jt.setBottomStreet(j.getBottomStreet().getId());
				if(j.getLeftStreet()!=null) jt.setLeftStreet(j.getLeftStreet().getId());
				if(j.getRightStreet()!=null) jt.setRightStreet(j.getRightStreet().getId());
				if(j.getTopStreet()!=null) jt.setTopStreet(j.getTopStreet().getId());
				jtl.getJunction().add(jt);
			}
			
			ct.setJunctions(jtl);

            /*
             * Extract data from Street list and add related Street information to the xml root element.
             */
			
			StreetsType stl = new StreetsType();

			for (Street s : streets){
				StreetType st = new StreetType();
				st.setId(s.getId());
				st.setOrientation(s.getOrientaion().toString());
				if (s.getStartJunction()!=null) st.setStartJunction(s.getStartJunction().getId());
				if (s.getEndJunction()!=null) st.setEndJunction(s.getEndJunction().getId());
				stl.getStreet().add(st);
			}
			
			ct.setStreets(stl);

			/*
			 * Extract data from Lane list (which is built through Street list)
			 * and add related Lane information to the xml root element.
			 */
			
			LanesType ltl = new LanesType();
			
			for (Street s : streets){
				for (Lane l : s.getLanes()){
					lanes.add(l);
				}
			}
			
			for (Lane l : lanes){
				LaneType lt = new LaneType();
				lt.setDirection(l.getLaneOrientation().toString());
				lt.setId(l.getId());
				lt.setStreet(l.getStreet().getId());
				ltl.getLane().add(lt);
			}
			
			ct.setLanes(ltl);

            /*
             * Extract data from Route list and add related Route information to the xml root element.
             */
			
			RoutesType rtl = new RoutesType();
			
			if (!routes.isEmpty())
			{
				for (Route r : routes)
				{
					RouteType rt = new RouteType();
					rt.setId(r.getId());
					for (Lane l : r.getLanes())
					{
						rt.getLane().add(l.getId());
					}
					
					rtl.getRoute().add(rt);
				}
			}
			
			ct.setRoutes(rtl);
			
			// marschal everything in the selected path.
			
			m.marshal(configElement,new FileOutputStream(xmlFilePath));

		} catch (JAXBException je) {
			je.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
