import Ember from 'ember';
import StyleableMixin from 'webui/mixins/components/styleable';

export default Ember.Component.extend(StyleableMixin, {

  classNames: ['component-layout-vertical-box'],
  classNameBindings: ['extraClass']

});
