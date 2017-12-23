import Ember from 'ember';
import AutoRegisterMixin from 'webui/mixins/components/autoregister'

export default Ember.Mixin.create(AutoRegisterMixin, {

  tagName: '',
  model: undefined,

  commit() {
    return this.get('fields').commit();
  },

  cancel() {
    return this.get('fields').cancel();
  }
});
