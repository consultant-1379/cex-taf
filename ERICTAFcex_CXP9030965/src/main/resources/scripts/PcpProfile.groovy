import java.util.ArrayList;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.pcp.service.IProfileMgtService;
import com.ericsson.oss.pcp.service.domain.Profile;
import com.ericsson.oss.pcp.service.domain.ProfileAttribute;
import com.ericsson.oss.pcp.service.domain.ProfileProperties;
import com.ericsson.oss.pcp.service.domain.ProfileProperties.ProfilePropertiesBuilder;

class PcpProfile {


	private IProfileMgtService service;
	public String planName = "taf";

	public def createProfile(String profileName,String fdn){

		String result = profileName;
		try {

			ProfilePropertiesBuilder builder = new ProfilePropertiesBuilder(profileName, System.getProperty("user.name"));
			sleep(1000);
			builder.setDescription("taf_test_description");
			ProfileProperties profileProperties = builder.build();

			ArrayList<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>(0);
			profileAttributes.add(new ProfileAttribute("ERBS_NODE_MODEL","ENodeBFunction,EUtranCellFDD,UtranFreqRelation,UtranCellRelation","adjacentCell", fdn));

			Profile profile = new Profile(profileProperties, profileAttributes);

			getprofileMgtUICoreService().createProfile(profile, System.getProperty("user.name"));
			sleep(100);
		} catch (Exception e) {
			result = e.getMessage().toString();
		}

		return result;
	}

	public def createProfMimMangr(String fdn){

		String result = "Profile_Created_Successfully_from_Mim_Manager";

		try {

			ProfilePropertiesBuilder builder = new ProfilePropertiesBuilder("taf_profile_with_Mim_Manager", System.getProperty("user.name"));
			sleep(1000);
			builder.setDescription("taf_test_description");
			ProfileProperties profileProperties = builder.build();

			ArrayList<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>(0);
			profileAttributes.add(new ProfileAttribute("ERBS_NODE_MODEL","ENodeBFunction,EUtranCellFDD,UtranFreqRelation,UtranCellRelation","adjacentCell", fdn));

			Profile profile = new Profile(profileProperties, profileAttributes);

			getprofileMgtUICoreService().createProfile(profile, System.getProperty("user.name"));
			sleep(100);
		} catch (Exception e) {
			result = e.getMessage().toString();
		}

		return result;
	}
	public def deleteProfMimMangr(){

		String result = "Profile_delete_Successfully_from_Mim_Manager";

		try{
			getprofileMgtUICoreService().deleteProfileByName("taf_profile_with_Mim_Manager", "nmsadm");
			sleep(1000);
		}catch (Exception e) {
			result = e.getMessage().toString();
		}

		return result;
	}
	public def createProfileExMo() {
		String result = "Create Prof Exist MO";


		try          {
			ProfilePropertiesBuilder builder = new ProfilePropertiesBuilder("taf_profile_Exist_Mo", System.getProperty("user.name"));

			builder.setDescription("taf_test_description");
			ProfileProperties profileProperties = builder.build();

			ArrayList<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>(0);
			profileAttributes.add(new ProfileAttribute("ERBS_NODE_MODEL","ENodeBFunction,EUtranCellFDD","administrativeState", "LOCKED"));
			Profile profile = new Profile(profileProperties, profileAttributes);

			getprofileMgtUICoreService().createProfile(profile, System.getProperty("user.name"));
		} catch (Exception e) {
			result = e.getMessage().toString();
		}

		return result;
	}
	public def deleteProfileExMo(){

		String result = "Profile_delete_Successfully_for_Exist_Mo";

		try{
			getprofileMgtUICoreService().deleteProfileByName("taf_profile_Exist_Mo", "nmsadm");
		}catch (Exception e) {
			result = e.getMessage().toString();
		}

		return result;
	}
	public def createProfileExMoMore(String fdn) {

		String result = "Create Prof Exist MO More";

		try          {
			ProfilePropertiesBuilder builder = new ProfilePropertiesBuilder("taf_profile_with_Exist_Mo_More", System.getProperty("user.name"));

			builder.setDescription("taf_test_description");
			ProfileProperties profileProperties = builder.build();

			ArrayList<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>(0);
			//profileAttributes.add(new ProfileAttribute(mimModel, moc, attrName, attrValue);

			profileAttributes.add(new ProfileAttribute("ERBS_NODE_MODEL","ENodeBFunction,EUtranCellFDD,UtranFreqRelation,UtranCellRelation","adjacentCell", fdn));
			profileAttributes.add(new ProfileAttribute("ERBS_NODE_MODEL","ENodeBFunction,EUtranCellFDD,UtranFreqRelation,UtranCellRelation","isHoAllowed", "true"));
			//profileAttributes.add(new profileAttributes("ERBS_NODE_MODEL","ENodeBFunction,EUtranCellFDD,UtranFreqRelation,UtranCellRelation","adjacentCell","d"));
			Profile profile = new Profile(profileProperties, profileAttributes);



			getprofileMgtUICoreService().createProfile(profile, System.getProperty("user.name"));

		} catch (Exception e) {
			result = e.getMessage().toString();
		}

		return result;
	}
	public def deleteProfileExMoMore(){

		String result = "Profile_delete_Successfully_for_Exist_Mo_More";

		try{
			getprofileMgtUICoreService().deleteProfileByName("taf_profile_with_Exist_Mo_More", "nmsadm");
			sleep(1000);
		}catch (Exception e) {
			result = e.getMessage().toString();
		}

		return result;
	}
	public def duplicateProfileExMo() {

		String result = "A profile with the same name already exists";
		try          {
			ProfilePropertiesBuilder builder = new ProfilePropertiesBuilder("taf_profile_with_Mim_Manager", System.getProperty("user.name"));

			builder.setDescription("taf_test_description");
			ProfileProperties profileProperties = builder.build();

			ArrayList<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>(0);
			profileAttributes.add(new ProfileAttribute("ERBS_NODE_MODEL","ENodeBFunction,EUtranCellFDD","administrativeState", "LOCKED"));
			Profile profile = new Profile(profileProperties, profileAttributes);

			getprofileMgtUICoreService().createProfile(profile, System.getProperty("user.name"));
		} catch (Exception e) {
			return result;
		}
		return "This should not happen";
	}

	//=======================================================================================================================//
	/*
	 * Services
	 */

	public def getprofileMgtUICoreService(){
		final String profileMgtUICoreUrl  = "rmi://masterservice:50042/ProfileManagementService";
		service= (IProfileMgtService) getRmiService(profileMgtUICoreUrl, IProfileMgtService.class);
		return service;
	}


	private static Object getRmiService(final String url, final Class clazz) {
		final RmiProxyFactoryBean plannedManagementRmiFactory = new RmiProxyFactoryBean();
		plannedManagementRmiFactory.setServiceInterface(clazz);
		plannedManagementRmiFactory.setServiceUrl(url);
		plannedManagementRmiFactory.setRefreshStubOnConnectFailure(true);
		plannedManagementRmiFactory.afterPropertiesSet();

		return ((FactoryBean) plannedManagementRmiFactory).getObject();
	}
}

