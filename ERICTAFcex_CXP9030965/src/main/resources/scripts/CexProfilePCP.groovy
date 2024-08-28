import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.pcp.service.IProfileMgtService;
import com.ericsson.oss.pcp.service.domain.Profile;
import com.ericsson.oss.pcp.service.domain.ProfileAttribute;
import com.ericsson.oss.pcp.service.domain.ProfileProperties;
import com.ericsson.oss.cp.core.OCP;
import java.util.Collections;

class CexProfilePCP {
	private IProfileMgtService service;
	final String PASSED = "OK"
	
	/*
	 * Creating a PCP profile
	 */
		public def createProfile(String name, String desc) {
		try	{
			final Collection<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>();
			final ProfileProperties profileProperties = new ProfileProperties.ProfilePropertiesBuilder(null, name, OCP.getUserId()).setDescription(desc).setModifiedBy(OCP.getUserId()).build();
		final Profile profile = new Profile(profileProperties, profileAttributes);
		getprofileMgtUICoreService().createProfile(profile, OCP.getUserId());
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	 return PASSED;
	}
		/*
		 * Finding Profile based on name of Profile Created earlier
		 */
		public def findProfileByName(String name) {
		try	{
		final Profile profile = getprofileMgtUICoreService().findProfileByName(name, OCP.getUserId());
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	 return PASSED;
	}
		/*
		 * Deleting a created Profile
		 */
		public def deleteProfileByName(String newProfileName) {
			try	{
			final Profile profile = getprofileMgtUICoreService().deleteProfileByName(newProfileName, OCP.getUserId());
			}
			catch(Exception e){
				return e.getMessage().toString();
			}
		 return PASSED;
		}
		
		/*
		 * updating Profile Already Created
		 */
		public def updateProfile(String originalName) {
			
				String newProfileName = "pcpPro2";
				String newProfileDesc = "EditedProfile";
//				String[] profileData = {PASSED, newProfileName, newProfileDesc};
			try	{
				final Collection<ProfileAttribute> profileAttributes = new ArrayList<ProfileAttribute>();
	
				final Profile profile = getprofileMgtUICoreService().findProfileByName(originalName, OCP.getUserId());
				ProfileProperties originalProfile = profile.getProfileProperties();
				
				final ProfileProperties profileProperties = new ProfileProperties.ProfilePropertiesBuilder(originalProfile
					.getId(), newProfileName, originalProfile.getOwnedBy()).setDescription(newProfileDesc).setCreatedTime(
					originalProfile.getCreatedTime()).setModifiedBy(OCP.getUserId()).build();
				
				final Profile profileForUpdate = new Profile(profileProperties, profileAttributes);
				
				//calling ProfileMgmnt Service to update Profile with new name and description
				
				getprofileMgtUICoreService().updateProfile(profileForUpdate, OCP.getUserId());
				
				//clean up
				
				deleteProfileByName(newProfileName);
			}
			catch(Exception e){
				return e.getMessage().toString();
			}
		 return PASSED;
		}
		
	
	
	//=======================================================================================================================//	
	/*
	 * Services
	 */
	
	public def getprofileMgtUICoreService(){
		final String profileMgtUICoreUrl = "rmi://masterservice:50042/ProfileManagementService";
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
