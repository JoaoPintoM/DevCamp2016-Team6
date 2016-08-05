import { StyleSheet, Platform } from 'react-native';
import _                        from'lodash';

if(Platform.OS == 'android') {
  titleContainerPlatform = {
    position: 'absolute',
    alignItems: 'flex-start',
    left: 64,
  }
}
else
  titleContainerPlatform = {}

module.exports = StyleSheet.create({
  navBar: {
    height: 64,
    backgroundColor: '#20B7AF',
    flex: 1
  },
  titleContainer: _.merge({
    flex: 1,
    paddingBottom: 15,
    //borderWidth: 1,
  }, titleContainerPlatform),
  title: {
    textAlign: 'left',
    color: 'white',
    marginTop: 18,
    fontSize: 18,
    fontWeight: '500',
  },
  leftIconContainer: {
    marginLeft: Platform.OS == 'android' ? 8 : 2,
    paddingTop: 18.5,
    paddingLeft: 8,
    paddingRight: 8,
    paddingBottom: 15.5
  },
  leftIcon: {
    fontSize: 24,
    color: 'white',
    fontWeight: '300'
  }
});