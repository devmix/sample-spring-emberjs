import Mixin from '@ember/object/mixin';

export default Mixin.create({

  id: undefined,
  title: undefined,
  direction: undefined,
  sortable: undefined,

  sortIcon: Ember.computed('direction', function () {
    const direction = this.get('direction');
    return direction === 'asc' ? 'sort content ascending'
      : (direction === 'desc' ? 'sort content descending' : undefined) || 'sort';
  }),

  sortApplyConfig(target) {
    if (this.sortable && this.direction) {
      target[this.id] = this.direction;
      return true;
    }
  },

  sortToggle() {
    const oldDir = this.get('direction');
    this.set('direction', oldDir === 'asc' ? 'desc' : (oldDir === 'desc' ? undefined : 'asc'));
  }
});
