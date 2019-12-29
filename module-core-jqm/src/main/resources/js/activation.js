var A = {
    adapterActivations: null,

    GetAdapterActivations: function () {
        if (this.adapterActivations == null) {
            this.adapterActivations = ajaxInit('/activation/adapteractivations.json');
        }
        return this.adapterActivations;
    }
}
