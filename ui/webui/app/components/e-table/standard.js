import Ember from 'ember';
import PageableMixin from '../../mixins/components/e-paging/pageable';
import SearchableMixin from '../../mixins/components/e-searching/searchable';
import StyleableMixin from '../../mixins/components/styleable';
import AutoRegisterMixin from '../../mixins/components/autoregister'

const { computed } = Ember;

export default Ember.Component.extend(StyleableMixin, PageableMixin, SearchableMixin, AutoRegisterMixin, {

  classNames: ['components-e-table', 'components-e-table-standard'],

  // properties
  columnsConfig: [],
  rows: [],
  modelName: undefined,
  filter: undefined,

  //
  table: undefined,
  store: Ember.inject.service(),

  computedRows: computed('rows', 'modelName', 'filter', function() {
    const modelName = this.get('modelName');
    if (modelName) {
      return this.watchPaging(this.get('store').query(modelName, this.get('filter')));
    }
    return this.get('rows');
  }),

  filter: computed('search', 'page', 'pageSize', function() {
    return {
      page: this.get('page'),
      size: this.get('pageSize'),
      search: this.get('search')
    }
    return {};
  }),

  onRefresh() {
    this.notifyPropertyChange('filter');
  },

  invokeAction(action, data) {
    this.sendAction('onCustomAction', action, data);
  },

  registerTableComponent(table) {
    this.set('table', table);
  },

  unregisterTableComponent() {
    this.set('table', undefined);
  }
});
