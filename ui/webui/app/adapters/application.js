import DS from 'ember-data';
import ENV from '../config/environment';

export default DS.RESTAdapter.extend({
//export default DS.JSONAPIAdapter.extend({
  namespace: 'api/core/entity',
  host: ENV.host,
  pathForType: function(modelName) {
    const hypen = modelName.indexOf('-');
    if (hypen !== -1) {
      const scope = modelName.substr(0, hypen),
            type = modelName.substr(hypen + 1);
      return `${scope}/${type}`;
    }
    return this._super(modelName);;
  }
});
