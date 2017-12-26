import Ember from 'ember';
import StyleableMixin from 'webui/mixins/components/styleable';

export default Ember.Component.extend(StyleableMixin, {
  classNames: ['e-layout-horizontal-box'],
  classNameBindings: ['extraClass']
});
