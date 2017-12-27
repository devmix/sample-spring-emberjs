import Ember from 'ember';
import SecuredRouteMixin from 'ember-simple-auth/mixins/authenticated-route-mixin';

export default Ember.Route.extend(SecuredRouteMixin);
